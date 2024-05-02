signal sigA, sigB, sigC, sigN: std_logic_vector(2 downto 0);
signal a, b, c, d, e, f: bit_vector(3 downto 1);

a <= ((b and c) and f) or (a and b) after 3;
sigC <= sigA and sigB after 6;
sigN <= sigA xor sigC after 2;