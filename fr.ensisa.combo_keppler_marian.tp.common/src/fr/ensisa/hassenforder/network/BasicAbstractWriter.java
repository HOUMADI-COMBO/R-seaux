package fr.ensisa.hassenforder.network;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class BasicAbstractWriter {

    protected OutputStream outputStream;
    protected ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private DataOutputStream output = new DataOutputStream(baos);

    public BasicAbstractWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    protected void writeBoolean(boolean v) {
        try {
            if (v) {
                output.writeInt(1);
            } else {
                output.writeInt(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeByte(byte v) {
        try {
            output.writeByte(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeShort(short v) {
        try {
            output.writeShort(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeInt(int v) {
        try {
            output.writeInt(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeLong(long v) {
        try {
            output.writeLong(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeFloat(float v) {
        try {
            output.writeFloat(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeDouble(double v) {
        try {
            output.writeDouble(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void writeString(String v) {
        try {
            output.writeUTF(v);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send() {
        byte[] message = baos.toByteArray();
        try {
            outputStream.write(message);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
