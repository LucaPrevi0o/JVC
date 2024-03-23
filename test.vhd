signal sigA, sigB, sigC, sigN : 6;

sigA <= "001100";
sigB <= not sigA after 3;
sigC <= sigA or sigB after 7;
sigN <= sigC and sigA after 2;