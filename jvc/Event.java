package jvc;

public class Event {
    
    private Signal target, source1=null, source2=null;
    private String token;
    private int time;
    
    public int getTime() { return this.time; }

    public void operation() {

        if (token.equals("not")) target.set(Signal.not(source1).getData());
        if (token.equals("and")) target.set(Signal.and(source1, source2).getData());
        if (token.equals("or")) target.set(Signal.or(source1, source2).getData());
        if (token.equals("xor")) target.set(Signal.xor(source1, source2).getData());
        if (token.equals("nand")) target.set(Signal.nand(source1, source2).getData());
        if (token.equals("nor")) target.set(Signal.nor(source1, source2).getData());
        if (token.equals("xnor")) target.set(Signal.xnor(source1, source2).getData());
        if (FileParser.isBinary(token)) target.set(Signal.assign(token).getData());
    }

    public Event(Signal s1, Signal s2, Signal t, String o, int ts) {

        this.source1=s1;
        this.source2=s2;
        this.target=t;
        this.token=o;
        this.time=ts;
    }

    public String toString() {

        return "New Event(timestamp "+time+"):\nTarget is "+target.toString()+
            "\nSource is ("+(source1==null ? "null" : source1.toString())+" - "+(source2==null ? "null" : source2.toString());

    }
}
