grails {
	plugin {
		springsecurity {
			securityConfigType = "InterceptUrlMap"
			filterChain {
				chainMap = [
					//Stateless chain
					[
						pattern: '/**',
						filters: 'JOINED_FILTERS,-anonymousAuthenticationFilter,-exceptionTranslationFilter,-authenticationProcessingFilter,-securityContextPersistenceFilter,-rememberMeAuthenticationFilter'
					],
					//Traditional, stateful chain
					[
						pattern: '/stateful/**',
						filters: 'JOINED_FILTERS,-restTokenValidationFilter,-restExceptionTranslationFilter'
					]
				]
			}
			userLookup {
				userDomainClassName = 'intranet.backend.User'
				authorityJoinClassName = 'intranet.backend.UserSecurityRole'
			}
			authority {
				className = 'intranet.backend.SecurityRole'
			}
			interceptUrlMap = [
					[pattern: '/', access: ['permitAll']],
					[pattern: '/error', access: ['permitAll']],
					[pattern: '/index', access: ['permitAll']],
					[pattern: '/index.gsp', access: ['permitAll']],
					[pattern: '/shutdown', access: ['permitAll']],
					[pattern: '/assets/**', access: ['permitAll']],
					[pattern: '/**/js/**', access: ['permitAll']],
					[pattern: '/**/css/**', access: ['permitAll']],
					[pattern: '/**/images/**', access: ['permitAll']],
					[pattern: '/**/favicon.ico', access: ['permitAll']],
					[pattern: '/announcements/*',  access: ['ROLE_BOSS'], httpMethod: 'DELETE'],
					[pattern: '/announcements',  access: ['ROLE_BOSS'], httpMethod: 'POST'],
					[pattern: '/announcements',  access: ['ROLE_BOSS', 'ROLE_EMPLOYEE']],
					[pattern: '/announcements/*',  access: ['ROLE_BOSS', 'ROLE_EMPLOYEE']],
					[pattern: '/api/login',  access: ['ROLE_ANONYMOUS']],
					[pattern: '/oauth/access_token',  access: ['ROLE_ANONYMOUS']],
			]
		}
	}
}
