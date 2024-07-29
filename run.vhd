-- Example .vhd file for usage demonstration purposes

-- declare a list of signals
signal a, b, c, d: bit_vector(3 to 9);
signal e, f: bit;
signal h1, h2, h3: std_logic_vector (9 downto 3);

-- list of assignment expressions
h1 <= "001011" after 3 ps;
f <= not f and (not e and not f) or f after 1 ps;
e <= not (not f) and  e after 5 ps;
c <= not (b and (not c or d) or a) or not (d and not (a and not b) or (a or not b and c)) after 3 ps;