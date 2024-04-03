package jvc.eventType;

public interface Event<SignalType> { //event interface
    
    //public methods (returning specified data type)
    public int getTime();
    public SignalType getTarget();
    public String getToken();
    public SignalType[] getSources();
    public void setTarget(SignalType t);
    public void setSources(SignalType[] s);
    public String toString();
    public void operation();
}
