signal sigA, sigB, sigC, sigN : 6;

sigA <= "001100" after 1;
sigC <= "001000" after 2;
sigB <= not sigA and sigC after 3;
sigC <= sigA or sigB after 7;
sigN <= sigC and sigA after 2;