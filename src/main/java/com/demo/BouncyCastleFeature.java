package com.demo;

import com.oracle.svm.core.annotate.AutomaticFeature;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;

@AutomaticFeature
public class BouncyCastleFeature implements Feature {

	@Override
	public void afterRegistration(AfterRegistrationAccess access) {
		RuntimeClassInitialization.initializeAtBuildTime("org.bouncycastle");
		//Security.addProvider(new BouncyCastleProvider());
		EnvironmentConfigurer.configure();
	}

}