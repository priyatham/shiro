package com.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ConvertObject {
	public static byte[] convertObjectToByteArray(Object object) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);

        oos.writeObject(object);
        oos.flush();
        oos.close();
        bos.close();

        byte[] data = bos.toByteArray();
        return data;
	}
	
	public static Object convertByteArrayToObject(byte[] bytes) throws IOException, ClassNotFoundException{
		
		Object object = null;
		
		ByteArrayInputStream bais;

        ObjectInputStream ins;


         bais = new ByteArrayInputStream(bytes);

         ins = new ObjectInputStream(bais);

         object = ins.readObject();

         ins.close();
         
		return object;
	}
	
}
