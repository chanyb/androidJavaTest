package com.example.smartbox_dup.utils;

import com.example.smartbox_dup.screen.function.alarm.Alarm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ByteArrayManager<T> {
    private T obj;

    public T getClassFromByteArray(byte[] bytes) {
        if(bytes == null) return null;

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            obj = (T) in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                obj = null;
            }
        }

        return obj;
    }

    public byte[] getByteArrayFromClassObject(T _obj) {
        if(_obj == null) return null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        byte[] bytes = null;

        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(_obj);
            out.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }
}
