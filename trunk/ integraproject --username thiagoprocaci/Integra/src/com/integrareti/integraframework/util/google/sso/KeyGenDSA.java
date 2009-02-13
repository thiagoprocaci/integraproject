package com.integrareti.integraframework.util.google.sso;

import java.io.FileOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

public class KeyGenDSA {
	public static void main(String[] args) {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator
					.getInstance("DSA", "SUN");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
			keyGen.initialize(1024, random);

			KeyPair pair = keyGen.generateKeyPair();
			PrivateKey priv = pair.getPrivate();
			PublicKey pub = pair.getPublic();

			byte[] encPriv = priv.getEncoded();
			FileOutputStream privfos = new FileOutputStream("DSAPrivateKey.key");
			privfos.write(encPriv);
			privfos.close();

			byte[] encPub = pub.getEncoded();
			FileOutputStream pubfos = new FileOutputStream("DSAPublicKey.key");
			pubfos.write(encPub);
			pubfos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
