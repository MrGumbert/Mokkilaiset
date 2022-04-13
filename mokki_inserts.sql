INSERT INTO `alue` (`alue_id`, `nimi`) VALUES
(1, 'Kittilä'),
(2, 'Kolari'),
(3, 'Inari');

INSERT INTO `posti` (`postinro`, `toimipaikka`) VALUES
('00000', 'Rikki'),
('00100', 'HELSINKI'),
('20810', 'TURKU'),
('70150', 'KUOPIO'),
('95970', 'ÄKÄSLOMPOLO'),
('99130', 'SIRKKA'),
('99140', 'KÖNGÄS'),
('99830', 'SAARISELKA');

INSERT INTO `asiakas` (`asiakas_id`, `postinro`, `etunimi`, `sukunimi`, `lahiosoite`, `email`, `puhelinnro`, `postituslista`) VALUES
(1, '70150', 'Tessi', 'Testaaja', 'Testikuja 1', 'tessi.testaaja@testi.com', '0401231123', 0),
(2, '70150', 'Testi', 'Testaaja', 'Testikuja 2', 'testi.testaaja@testi.com', '0501231123', 1),
(3, '00100', 'John', 'Doe', 'Katukuja 1', 'john.doe@test.com', '0403213321', 0),
(4, '00100', 'Jane', 'Doe', 'Katukuja 2', 'jane.doe@test.com', '0503213321', 1),
(5, '99830', 'Jake', 'Jacobs', 'Streetname 1', 'jake.j@test.com', '1230401231', 0),
(6, '70150', 'Pekka', 'Pekkanen', 'Streetname 2', 'pekka.p@test.com', '0401233211', 1),
(8, '95970', 'Testa', 'ataa', 'Testaa 2', 'testataa@test.com', '0104040124', 0),
(10, '95970', 'Taa', 'Taata', 'Taatata 1', 'taata@test.com', '0605051234', 1);

INSERT INTO `mokki` (`mokki_id`, `alue_id`, `postinro`, `mokkinimi`, `katuosoite`, `hinta`, `kuvaus`, `henkilomaara`, `varustelu`) VALUES
(1, 1, '99130', 'Levin Aurinko', 'Alakuuntie 2', 80.00, 'Levin Etelärinteillä tyylikkäästi kalustettu lomahuoneisto.', 4, 'Pyykinpesukone, silitysrauta, kuivauskaappi'),
(2, 1, '99140', 'Köngäs-Pirtti', 'Sikkolantie 6', 50.00, 'Viihtyisä ja rauhallinen mökki luonnon keskellä Könkäässä.', 8, 'Pyykinpesukone, hiustenkuivaaja, silitysrauta'),
(3, 1, '99130', 'Kuutonen', 'Ratsastajankuja 1', 45.00, 'Levin ytimessä rinteiden ja latujen vieressä mukava loma-asunto.', 5, 'Silitysrauta ja -lauta, hiustenkuivaaja, suihku'),
(4, 2, '95970', 'Kelohuoneisto Feeling', 'Kesänkijärventie 4 A 3', 212.00, 'Kelohuoneisto seitsemän tunturin ympäröimässä kylässä.', 7, 'Pyykinpesukone, hiustenkuivaaja, kuivauskaappi'),
(5, 1, '99130', 'Taalonranta', 'Taalonranta 10', 65.00, 'Tasokas mökki Levillä.', 11, 'Kuivausrumpu, pyykinpesukone, kuivauskaappi'),
(6, 3, '99830', 'Vasamooli', 'Vahtamantie 12 as 5', 60.00, 'Kelohuoneisto Saariselän keskustassa.', 4, 'Astianpesukone, jääkaappipakastin, mikroaaltouuni');

INSERT INTO `varaus` (`varaus_id`, `asiakas_id`, `mokki_id`, `varattu_pvm`, `vahvistus_pvm`, `varattu_alkupvm`, `varattu_loppupvm`) VALUES
(1, 1, 1, '2022-04-08 04:10:10', NULL, '2022-04-10 12:00:00', '2022-04-17 12:00:00'),
(2, 2, 2, '2022-04-08 04:10:10', '2022-04-08 04:20:10', '2022-04-10 12:00:00', '2022-04-17 12:00:00'),
(3, 3, 3, '2022-04-08 04:10:10', NULL, '2022-04-10 12:00:00', '2022-04-17 12:00:00'),
(4, 4, 4, '2022-04-08 04:10:10', '2022-04-08 04:20:10', '2022-04-10 12:00:00', '2022-04-17 12:00:00'),
(5, 5, 6, '2022-04-08 04:10:10', '2022-04-08 04:20:10', '2022-04-10 12:00:00', '2022-04-17 12:00:00');

INSERT INTO `palvelu` (`palvelu_id`, `alue_id`, `nimi`, `tyyppi`, `kuvaus`, `hinta`, `alv`) VALUES
(1, 1, 'Moottorikelkkailu', 1, 'Moottorikelkalla ajaminen luonnossa.', 50.00, 24.00),
(2, 1, 'Ratsastus', 1, 'Sopii kokemattomille ja kokeneille ratsastajille.', 60.00, 24.00),
(3, 1, 'Hiihto', 2, 'Hiihtoladut lähellä.', 10.00, 24.00),
(4, 1, 'Laskettelu', 2, 'Laskettelukeskus lähellä.', 40.00, 24.00),
(5, 2, 'Moottorikelkkailu', 1, 'Moottorikelkalla ajaminen luonnossa.', 50.00, 24.00),
(6, 2, 'Ratsastus', 1, 'Sopii kokemattomille ja kokeneille ratsastajille.', 60.00, 24.00),
(7, 2, 'Hiihto', 2, 'Hiihtoladut lähellä.', 10.00, 24.00),
(8, 3, 'Laskettelu', 2, 'Laskettelukeskus lähellä.', 40.00, 24.00),
(9, 3, 'Hiihto', 2, 'Hiihtoladut lähellä.', 10.00, 24.00);

INSERT INTO `lasku` (`lasku_id`, `varaus_id`, `summa`, `alv`, `maksettu`, `erapaiva`, `laskupaiva`, `laskutyyppi`) VALUES
(1, 1, 560.00, 24.00, 0, '2022-04-25', '2022-04-08', 'sposti'),
(2, 2, 350.00, 24.00, 0, '2022-04-25', '2022-04-08', 'sposti'),
(3, 3, 315.00, 24.00, 0, '2022-04-25', '2022-04-08', 'sposti'),
(4, 4, 1484.00, 24.00, 0, '2022-04-25', '2022-04-08', 'posti'),
(5, 5, 420.00, 24.00, 0, '2022-04-25', '2022-04-08', 'posti');

INSERT INTO `varauksen_palvelut` (`varaus_id`, `palvelu_id`, `lkm`) VALUES
(1, 1, 3),
(2, 2, 3),
(3, 3, 3),
(4, 5, 3),
(5, 8, 3);
