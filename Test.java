import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import jvc.Signal;
import jvc.signalType.Type;
import jvc.signalType.types.logic.Bit;
import jvc.signalType.types.logic.StdLogic;

public class Test {

    private static void doWrite(Type d, ObjectOutputStream oos) {

        try {
            
            if (d.equals(Bit.TRUE)) oos.writeObject("Bit.TRUE");
            else if (d.equals(Bit.FALSE)) oos.writeObject("Bit.FALSE");
            else if (d.equals(StdLogic.T)) oos.writeObject("StdLogic.T");
            else if (d.equals(StdLogic.F)) oos.writeObject("StdLogic.F");
            else if (d.equals(StdLogic.X)) oos.writeObject("StdLogic.X");
            else if (d.equals(StdLogic.U)) oos.writeObject("StdLogic.U");
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static Type doRead(String d) {

        if (d.equals("Bit.TRUE")) return Bit.TRUE;
        if (d.equals("Bit.FALSE")) return Bit.FALSE;
        if (d.equals("StdLogic.T")) return StdLogic.T;
        if (d.equals("StdLogic.F")) return StdLogic.F;
        if (d.equals("StdLogic.U")) return StdLogic.U;
        if (d.equals("StdLogic.X")) return StdLogic.X;
        return null;
    }
    
    public static void main(String[] args) {
        
        var test=new Signal<Type>("testSig", new Type[]{Bit.FALSE, Bit.TRUE}, new int[]{1, 2});
        var dataFile=new File("./sim/test.vhdata");
        try {

            dataFile.createNewFile();
            var outFos=new FileOutputStream(dataFile);
            var outOos=new ObjectOutputStream(outFos);
            System.out.println("Writing: "+test);
            outOos.writeObject(test.getName());
            outOos.writeObject(test.getValue().length);
            for (var i=0; i<test.getValue().length; i++) {

                doWrite(test.getValue()[i], outOos);
                outOos.writeObject(test.getIndexes()[i]);
            }
            outOos.flush();
            outOos.close();

            var inFos=new FileInputStream(dataFile);
            var inOos=new ObjectInputStream(inFos);

            var name=(String)inOos.readObject();
            var length=(int)inOos.readObject();
            var data=new Type[length];
            var indexes=new int[length];
            for (var i=0; i<length; i++) {

                var d=(String)inOos.readObject();
                data[i]=doRead(d);
                indexes[i]=(int)inOos.readObject();
            }

            inOos.close();
            var newTest=new Signal<>(name, data, indexes);
            System.out.println("Reading: "+newTest);
        } catch (Exception e) { e.printStackTrace(); }
    }
}
