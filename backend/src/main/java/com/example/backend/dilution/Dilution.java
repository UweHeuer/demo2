package com.example.backend.dilution;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Dilution {
	
	private Key key = null;
	private String verfahren = "AES";	
	private String parole;

	
	public Dilution(@Value("${uweheuer.bookmarks.parole}") String parole) {
		this.parole = parole;
		this.key = new SecretKeySpec(this.parole.getBytes(), verfahren);		
	}
	
	
	public OutputStream encryptOutputStream(OutputStream os) 
		throws Exception 
	{
		// check integrity
		valid();

		Cipher cipher = Cipher.getInstance(verfahren);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		os = new CipherOutputStream(os, cipher);

		return os;
	}	

	
	public InputStream decryptInputStream(InputStream is) 
		throws Exception 
	{
		// check integrity
		valid();

		Cipher cipher = Cipher.getInstance(verfahren);
		cipher.init(Cipher.DECRYPT_MODE, key);
		is = new CipherInputStream(is, cipher);

		return is;
	}
	
	
	public String encrypt(String text) throws Exception 
	{
		// check integrity
		valid();

		Cipher cipher = Cipher.getInstance(verfahren);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encrypted = cipher.doFinal(text.getBytes("UTF8"));

		return java.util.Base64.getEncoder().encodeToString(encrypted);
	}	
	
	
	public String decrypt(String geheim) throws Exception 
	{
		// check integrity
		valid();
 
	  byte[] crypted = java.util.Base64.getDecoder().decode(geheim);

		// decrypt
		Cipher cipher = Cipher.getInstance(verfahren);
		cipher.init(Cipher.DECRYPT_MODE, key);
		byte[] cipherData = cipher.doFinal(crypted);

		return new String(cipherData, "UTF8");
	}

	
	private boolean valid() throws Exception
	{
		if (verfahren == null)
		{
			throw new NullPointerException("Kein Verfahren angegeben!");
		}

		if (key == null)
		{
			throw new NullPointerException("Keinen Key angegeben!");
		}

		if (verfahren.isEmpty())
		{		
			throw new NullPointerException("Verfahren ist empty!");
		}

		return true;
	}	

	
	public String getParole() {
		return this.parole;
	}

}
