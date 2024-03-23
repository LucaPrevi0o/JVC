signal sigA, sigB, sigC, sigN : 6;

sigA <= "001100" after 1;
sigB <= not sigA after 2;
sigN <= "101010" after 9;
sigC <= sigA or sigB and sigN after 5;
sigN <= not sigN after 1;