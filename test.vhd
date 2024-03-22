signal sigA, sigB, sigC, sigN : 6;

sigA <= 001100;
sigB <= not sigA ;
sigC <= sigA or sigB;
sigN <= sigC and sigA;