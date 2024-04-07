signal sigA, sigB, sigC, sigN: bit_vector    ( 9 downto 4) ;
signal sigQ, sigW: std_logic_vector(0 to 9);
signal newS: bit;

sigA <= sigB and sigC;
sigN <= not(sigA or sigB);