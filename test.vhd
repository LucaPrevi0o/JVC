signal sigA, sigB, sigC, sigN : 6;


sigA <= 001100 ;
sigC <= 101010 ;
sigN <= not sigN ;
sigN <= not sigA or sigC xor sigN ;