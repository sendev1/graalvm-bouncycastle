package com.demo;

import com.oracle.svm.core.annotate.AutomaticFeature;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;

@AutomaticFeature
public class BouncyCastleFeature implements Feature {

	@Override
	public void afterRegistration(AfterRegistrationAccess access) {
		System.out.println("afterRegistration : READ ME");
		RuntimeClassInitialization.initializeAtBuildTime("org.bouncycastle");
		//Security.addProvider(new BouncyCastleProvider());
		EnvironmentConfigurer.configure();
	}
}