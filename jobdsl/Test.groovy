def allowedEnvironments = [
	"dev",
	"qa",
	"e2e",
	"test",
	"perf",
	"stage",
	"prod"
]
def nextEnvIndex = 1

for ( curEnv in allowedEnvironments ) {

	def nextEnv = (nextEnvIndex < allowedEnvironments.size ( ) ? allowedEnvironments[nextEnvIndex] : '' )
	nextEnvIndex++

	job ( "${YOUR_USER_NAME}Test${curEnv}" ) {

		description ( 'generated by Job DSL on ' + new Date ( ) )
		logRotator  ( -1, 50, -1, 50 )

		parameters {
			stringParam ( "ARTIFACT_S3_URL", "", "url for the app to be deployed" )
		}

		scm {
			git {
				branch ( 'origin/master' )
				remote {
					url         ( "${GIT_REPO_SSH_URL}"  )
					credentials ( "${GIT_CREDENTIAL_ID}" )
				}
			}
		}

		steps {
			shell (
	"""#!/bin/bash
	
echo
echo insert code here to execute your tests for your ${curEnv} environment
echo
	""")
		}

	if ( nextEnv != '' ) {
			publishers {
        		downstreamParameterized {
            			trigger( "${YOUR_USER_NAME}Deploy${nextEnv}") {
                		condition('UNSTABLE_OR_BETTER')
                		parameters {
                    		predefinedProp ( "ARTIFACT_S3_URL", "insert_s3_url" )
                		}
            		}
        		}
        	}
	}

	}

}
