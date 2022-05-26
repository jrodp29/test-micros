#!/usr/bin/env groovy
/**
 * Custom Properties
 */


def call(language, projectEnv, sourceRepositoryTypeRef) {
	return [
		[   // CI Steps
			type: "boolean",
			name: 'devopspaasMavenBuild',
			showWhen: projectEnv ==~ /dev|buildrc|rel/,
			defaultValue: devopspaasParametersLoader.defaultValueForNonFeatures(sourceRepositoryTypeRef)
		],[
			type: "boolean",
			name: 'devopspaasMavenUnitTest',
			showWhen: projectEnv ==~ /dev|buildrc|rel/,
			defaultValue: devopspaasParametersLoader.defaultValueForFeatures(sourceRepositoryTypeRef),
		],[
			type: "boolean",
			name: 'devopspaasMavenSonar',
			showWhen: projectEnv ==~ /dev|buildrc|rel/,
			defaultValue: devopspaasParametersLoader.defaultValueForNonFeatures(sourceRepositoryTypeRef),
		],[
			type: "boolean",
			name: 'devopspaasMavenDeploy',
			showWhen: projectEnv ==~ /dev|buildrc|rel/,
			defaultValue: devopspaasParametersLoader.defaultValueForFeatures(sourceRepositoryTypeRef),
		],[ // CD Steps
			type: "boolean",
			name: 'devopspaasKubeCD',
			showWhen: true,
			defaultValue: devopspaasParametersLoader.defaultValueForNonFeatures(sourceRepositoryTypeRef)
		],[
			type: "boolean",
			name: 'debug',
			showWhen: true,
			description: 'Switch on debug mode logger',
			defaultValue: true
		]
	]
}

return this