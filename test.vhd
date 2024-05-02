signal sigA, sigB, sigC, sigN: std_logic_vector(2 downto 0);
signal a, b, c, d, e, f: bit_vector(3 downto 1);

b <= "011";
c <= "001";
a <= ((b and c) and f) or not a and b;
sigA <= "110";
sigB <= "001";
sigC <= sigA and sigB;
sigN <= sigA xor sigC;