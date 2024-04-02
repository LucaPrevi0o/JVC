package jvc.eventType;

public interface Event<SignalType> {
    
    String token=""; //operator type
    int time=0; //time stamp for event
    
    public int getTime();
    public SignalType getTarget();
    public String getToken();
    public SignalType[] getSources();
    public void setTarget(SignalType t);
    public void setSources(SignalType[] s);
    public String toString();
    public void operation();
}
