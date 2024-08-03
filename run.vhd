-- Example .vhd file for usage demonstration purposes

-- declare a list of signals
signal a, b, c, d: bit_vector(3 to 9);
signal e, f: bit;

-- list of assignment expressions
f <= not f and (not e and not f) or f after 100 ns;
e <= not (not f) and  e after 50 ps;
c <= not (b nand (not c xor d) or a) or not (d nor not (a and not b) or (a xor not b and c)) after 0.2 ms;

-- declaration and assignment after previous assignment block
signal h1, h2, h3: std_logic_vector (9 downto 3);
h1 <= "001011" after 3 us;