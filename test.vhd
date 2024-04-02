signal sigA, sigB, sigC, sigN : std_logic;
signal newS : bit;

sigA <= "1" after 2;
sigB <= not sigA after 3;
sigC <= sigA or newS after 3;