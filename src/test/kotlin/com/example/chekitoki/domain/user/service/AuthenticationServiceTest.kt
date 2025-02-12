package com.example.chekitoki.domain.user.service

import com.example.chekitoki.api.exception.BusinessException
import com.example.chekitoki.config.auth.jwt.TokenProvider
import com.example.chekitoki.domain.fixtures.UserFixtures
import com.example.chekitoki.domain.token.model.RefreshToken
import com.example.chekitoki.domain.token.repository.RefreshTokenRepository
import com.example.chekitoki.domain.token.repository.RefreshTokenStore
import com.example.chekitoki.domain.user.exception.InvalidCredentialsException
import com.example.chekitoki.domain.user.repository.UserRepository
import com.example.chekitoki.domain.user.repository.UserStoreImpl
import com.example.chekitoki.utils.CookieUtils
import io.jsonwebtoken.Claims
import io.jsonwebtoken.impl.DefaultClaims
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import jakarta.servlet.http.Cookie
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes

class AuthenticationServiceTest : DescribeSpec({
    val tokenRepository: RefreshTokenRepository = mockk<RefreshTokenRepository>()
    val tokenStore = RefreshTokenStore(tokenRepository)
    val userRepository: UserRepository = mockk<UserRepository>()
    val userStore: UserStore = UserStoreImpl(userRepository)

    val authenticationManager: AuthenticationManager = mockk<AuthenticationManager>()
    val tokenProvider: TokenProvider = mockk<TokenProvider>()

    val authService = AuthenticationService(authenticationManager, tokenStore, tokenProvider, userStore)

    val accessToken = "access-token-value"
    val refreshToken = "refresh-token-value"

    lateinit var mockRequest: MockHttpServletRequest
    lateinit var mockResponse: MockHttpServletResponse

    beforeEach {
        mockRequest = MockHttpServletRequest()
        mockResponse = MockHttpServletResponse()
        RequestContextHolder.setRequestAttributes(ServletRequestAttributes(mockRequest, mockResponse))
    }

    describe("login") {
        context("정상적인 로그인 요청이 주어진 경우") {
            it("로그인에 성공하여 유저 정보를 반환한다.") {
                every { authenticationManager.authenticate(any()) } returns mockk<Authentication>()
                every { tokenProvider.generateToken(any(), true) } returns accessToken
                every { tokenProvider.generateToken(any(), false) } returns refreshToken
                every { userRepository.findByUserId(UserFixtures.userId) } returns UserFixtures.testUser
                every { tokenRepository.save(any()) } returns RefreshToken(UserFixtures.testUser, refreshToken)

                val result = authService.login(UserFixtures.LoginInfo)

                val accessTokenCookie = mockResponse.getCookie(TokenProvider.ACCESS_TOKEN)
                val refreshTokenCookie = mockResponse.getCookie(TokenProvider.REFRESH_TOKEN)

                result.userId shouldBe UserFixtures.userId
                accessTokenCookie shouldNotBe null
                accessTokenCookie!!.value shouldBe accessToken
                refreshTokenCookie shouldNotBe null
                refreshTokenCookie!!.value shouldBe refreshToken
            }
        }

        context("잘못된 정보로 로그인 유청이 주어진 경우") {
            it("로그인에 실패하여 인증 예외를 던진다.") {
                every { authenticationManager.authenticate(any()) } throws BadCredentialsException("Bad credentials")

                shouldThrow<InvalidCredentialsException> {
                    authService.login(UserFixtures.WrongLoginInfo)
                }
            }
        }
    }

    describe("logout") {
        context("로그아웃 요청이 주어진 경우") {
            beforeTest {
                mockRequest.setCookies(
                    Cookie(TokenProvider.ACCESS_TOKEN, accessToken),
                    Cookie(TokenProvider.REFRESH_TOKEN, refreshToken)
                )
            }
            it("로그아웃에 성공하여 토큰을 삭제한다.") {
                every { tokenRepository.deleteByToken(refreshToken) } just Runs

                authService.logout(UserFixtures.userId)

                val accessTokenCookie = mockResponse.getCookie(TokenProvider.ACCESS_TOKEN)
                val refreshTokenCookie = mockResponse.getCookie(TokenProvider.REFRESH_TOKEN)

                accessTokenCookie!!.maxAge shouldBe 0
                refreshTokenCookie!!.maxAge shouldBe 0
            }
        }

        context("로그인 되어 있지 않은 상태에서 로그아웃 요청이 주어진 경우") {
            it("로그아웃에 실패하여 예외를 던진다.") {
                shouldThrow<BusinessException> {
                    authService.logout(UserFixtures.userId)
                }
            }
        }
    }

    describe("reissue") {
        val mockClaims: Claims = DefaultClaims().apply {
            put("sub", UserFixtures.userId)
            put("role", listOf("USER"))
        }
        context("정상적인 토큰 재발급 요청이 주어진 경우") {
            beforeTest {
                mockRequest.setCookies(
                    Cookie(TokenProvider.ACCESS_TOKEN, "expired-$accessToken"),
                    Cookie(TokenProvider.REFRESH_TOKEN, refreshToken)
                )
            }
            it("토큰을 재발급한다.") {
                every { tokenProvider.validateTokenOrThrow(refreshToken) } just Runs
                every { tokenProvider.getClaims(refreshToken) } returns mockClaims
                every { userRepository.findByUserId(UserFixtures.userId) } returns UserFixtures.testUser
                every { tokenProvider.generateToken(any(), true) } returns "new$accessToken"
                every { tokenProvider.generateToken(any(), false) } returns "new$refreshToken"
                every { tokenRepository.findByToken(refreshToken) } returns RefreshToken(UserFixtures.testUser, refreshToken)

                authService.reissue()

                val accessTokenCookie = mockResponse.getCookie(TokenProvider.ACCESS_TOKEN)
                val refreshTokenCookie = mockResponse.getCookie(TokenProvider.REFRESH_TOKEN)

                accessTokenCookie!!.value shouldBe "new$accessToken"
                refreshTokenCookie!!.value shouldBe "new$refreshToken"
            }
        }

        context("쿠키에 저장된 토큰이 존재하지 않는 경우") {
            it("토큰 재발급에 실패하여 예외를 던진다.") {
                shouldThrow<CookieUtils.NoSuchCookieException> {
                    authService.reissue()
                }
            }
        }

        context("유효하지 않은 토큰이 주어진 경우") {
            beforeTest {
                mockRequest.setCookies(
                    Cookie(TokenProvider.ACCESS_TOKEN, "invalid-$accessToken"),
                    Cookie(TokenProvider.REFRESH_TOKEN, refreshToken)
                )
            }
            it("토큰 재발급에 실패하여 예외를 던진다.") {
                every { tokenProvider.validateTokenOrThrow(refreshToken) } throws TokenProvider.InvalidTokenException()

                shouldThrow<TokenProvider.InvalidTokenException> {
                    authService.reissue()
                }
            }
        }
    }
})
