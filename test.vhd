signal sigA, sigB, sigC, sigN: std_logic_vector(2 downto 0);
signal a, b, c, d, e, f: bit;

a <= not (b or c) and b or a;