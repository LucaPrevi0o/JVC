package jvc.dataType;

public interface Signal<SignalType extends Signal<SignalType, SignalDataType>, SignalDataType> { //SignalType template
    
    int length=0; //bit size

    //data returning methods
    public SignalDataType[] getData();
    public int getDimension();
    public String getName();

    //operations
    public SignalType not(SignalType s);
    public SignalType and(SignalType s1, SignalType s2);
    public SignalType or(SignalType s1, SignalType s2);
    public SignalType xor(SignalType s1, SignalType s2);
    public SignalType nand(SignalType s1, SignalType s2);
    public SignalType nor(SignalType s1, SignalType s2);
    public SignalType xnor(SignalType s1, SignalType s2);
    public SignalType assign(String val);
}