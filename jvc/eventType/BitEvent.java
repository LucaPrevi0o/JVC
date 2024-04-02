package jvc.eventType;
import jvc.*;
import jvc.dataType.*;

public class BitEvent implements Event<Bit> {
    
    private Bit target, source1=null, source2=null;
    private String token;
    private int time;
    
    public int getTime() { return this.time; }
    public Bit getTarget() { return this.target; }
    public String getToken() { return this.token; }
    public Bit[] getSources() {

        var res=new Bit[2];
        res[0]=source1;
        res[1]=source2;
        return res;
    }

    public void setTarget(Bit t) { this.target=t; }
    public void setSources(Bit[] s) {

        this.source1=s[0];
        this.source2=s[1];
    }

    public void operation() { //set target signal with updated data from declared operation

        var operator=new Bit(target.getName(), time); //dummy operator object (use non-static methods as static)
        if (token.equals("not")) target.set(operator.not(source2).getData());
        if (token.equals("and")) target.set(operator.and(source1, source2).getData());
        if (token.equals("or")) target.set(operator.or(source1, source2).getData());
        if (token.equals("xor")) target.set(operator.xor(source1, source2).getData());
        if (token.equals("nand")) target.set(operator.nand(source1, source2).getData());
        if (token.equals("nor")) target.set(operator.nor(source1, source2).getData());
        if (token.equals("xnor")) target.set(operator.xnor(source1, source2).getData());
        if (FileParser.isBinary(token)) target.set(operator.assign(token).getData());
    }

    public BitEvent(Bit s1, Bit s2, Bit t, String o, int ts) {

        this.source1=s1;
        this.source2=s2;
        this.target=t;
        this.token=o;
        this.time=ts;
    }

    public String toString() {

        return "New Event<"+Bit.class+">(timestamp "+time+"):\nTarget is "+target.toString()+
            "\nSource is ("+(source1==null ? "null" : source1.toString())+" - "+(source2==null ? "null" : source2.toString())+
            ")\nOperation is: "+token+"\n";
    }
}
