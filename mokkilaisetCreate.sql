-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 12, 2022 at 07:16 PM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `mokkilaiset`
--

-- --------------------------------------------------------

--
-- Table structure for table `alue`
--

CREATE TABLE `alue` (
  `alue_id` int(11) NOT NULL,
  `nimi` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `asiakas`
--

CREATE TABLE `asiakas` (
  `asiakas_id` int(11) NOT NULL,
  `postinro` char(5) DEFAULT NULL,
  `etunimi` varchar(20) NOT NULL,
  `sukunimi` varchar(40) NOT NULL,
  `lahiosoite` varchar(40) NOT NULL,
  `email` varchar(50) NOT NULL,
  `puhelinnro` varchar(15) NOT NULL,
  `postituslista` tinyint(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `lasku`
--

CREATE TABLE `lasku` (
  `lasku_id` int(11) NOT NULL,
  `varaus_id` int(11) DEFAULT NULL,
  `viitenumero` varchar(9) NOT NULL,
  `summa` double(8,2) NOT NULL,
  `alv` double(8,2) NOT NULL,
  `maksettu` bit(1) NOT NULL,
  `erapaiva` date NOT NULL,
  `laskupaiva` date DEFAULT NULL,
  `laskutyyppi` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `mokki`
--

CREATE TABLE `mokki` (
  `mokki_id` int(11) NOT NULL,
  `alue_id` int(11) DEFAULT NULL,
  `postinro` char(5) DEFAULT NULL,
  `mokkinimi` varchar(45) NOT NULL,
  `katuosoite` varchar(45) NOT NULL,
  `hinta` double(8,2) NOT NULL,
  `kuvaus` varchar(150) NOT NULL,
  `henkilomaara` int(11) NOT NULL,
  `varustelu` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `palvelu`
--

CREATE TABLE `palvelu` (
  `palvelu_id` int(11) NOT NULL,
  `alue_id` int(11) DEFAULT NULL,
  `nimi` varchar(40) NOT NULL,
  `tyyppi` varchar(25) NOT NULL,
  `kuvaus` varchar(225) NOT NULL,
  `hinta` double(8,2) NOT NULL,
  `alv` double(8,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- --------------------------------------------------------

--
-- Table structure for table `posti`
--

CREATE TABLE `posti` (
  `postinro` char(5) NOT NULL,
  `toimipaikka` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `varauksen_palvelut`
--

CREATE TABLE `varauksen_palvelut` (
  `varaus_id` int(11) DEFAULT NULL,
  `palvelu_id` int(11) DEFAULT NULL,
  `ajankohta` datetime NOT NULL,
  `lkm` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `varaus`
--

CREATE TABLE `varaus` (
  `varaus_id` int(11) NOT NULL,
  `asiakas_id` int(11) DEFAULT NULL,
  `mokki_id` int(11) DEFAULT NULL,
  `varattu_pvm` datetime NOT NULL,
  `vahvistus_pvm` datetime DEFAULT NULL,
  `varattu_alkupvm` datetime NOT NULL,
  `varattu_loppupvm` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `alue`
--
ALTER TABLE `alue`
  ADD PRIMARY KEY (`alue_id`);

--
-- Indexes for table `asiakas`
--
ALTER TABLE `asiakas`
  ADD PRIMARY KEY (`asiakas_id`),
  ADD KEY `postinro_idx` (`postinro`);

--
-- Indexes for table `lasku`
--
ALTER TABLE `lasku`
  ADD PRIMARY KEY (`lasku_id`),
  ADD KEY `varaus_id_idx` (`varaus_id`);

--
-- Indexes for table `mokki`
--
ALTER TABLE `mokki`
  ADD PRIMARY KEY (`mokki_id`),
  ADD KEY `alue_id_idx` (`alue_id`),
  ADD KEY `postinro_idx` (`postinro`);

--
-- Indexes for table `palvelu`
--
ALTER TABLE `palvelu`
  ADD PRIMARY KEY (`palvelu_id`),
  ADD KEY `alue_id_idx` (`alue_id`);

--
-- Indexes for table `posti`
--
ALTER TABLE `posti`
  ADD PRIMARY KEY (`postinro`);

--
-- Indexes for table `varauksen_palvelut`
--
ALTER TABLE `varauksen_palvelut`
  ADD KEY `varaus_id_idx` (`varaus_id`),
  ADD KEY `palvelu_id_idx` (`palvelu_id`);

--
-- Indexes for table `varaus`
--
ALTER TABLE `varaus`
  ADD PRIMARY KEY (`varaus_id`),
  ADD KEY `asiakas_id_idx` (`asiakas_id`),
  ADD KEY `mokki_id_idx` (`mokki_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `alue`
--
ALTER TABLE `alue`
  MODIFY `alue_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `asiakas`
--
ALTER TABLE `asiakas`
  MODIFY `asiakas_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `lasku`
--
ALTER TABLE `lasku`
  MODIFY `lasku_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `mokki`
--
ALTER TABLE `mokki`
  MODIFY `mokki_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `palvelu`
--
ALTER TABLE `palvelu`
  MODIFY `palvelu_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT for table `varaus`
--
ALTER TABLE `varaus`
  MODIFY `varaus_id` int(11) NOT NULL AUTO_INCREMENT;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `asiakas`
--
ALTER TABLE `asiakas`
  ADD CONSTRAINT `postinro_as` FOREIGN KEY (`postinro`) REFERENCES `posti` (`postinro`) ON DELETE SET NULL ON UPDATE NO ACTION;

--
-- Constraints for table `lasku`
--
ALTER TABLE `lasku`
  ADD CONSTRAINT `varaus_id_la` FOREIGN KEY (`varaus_id`) REFERENCES `varaus` (`varaus_id`) ON DELETE SET NULL ON UPDATE NO ACTION;

--
-- Constraints for table `mokki`
--
ALTER TABLE `mokki`
  ADD CONSTRAINT `alue_id_mo` FOREIGN KEY (`alue_id`) REFERENCES `alue` (`alue_id`) ON DELETE SET NULL ON UPDATE NO ACTION,
  ADD CONSTRAINT `postinro_mo` FOREIGN KEY (`postinro`) REFERENCES `posti` (`postinro`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `palvelu`
--
ALTER TABLE `palvelu`
  ADD CONSTRAINT `alue_id_pa` FOREIGN KEY (`alue_id`) REFERENCES `alue` (`alue_id`) ON DELETE SET NULL ON UPDATE NO ACTION;

--
-- Constraints for table `varauksen_palvelut`
--
ALTER TABLE `varauksen_palvelut`
  ADD CONSTRAINT `palvelu_id_vp` FOREIGN KEY (`palvelu_id`) REFERENCES `palvelu` (`palvelu_id`) ON DELETE SET NULL ON UPDATE NO ACTION,
  ADD CONSTRAINT `varaus_id_vp` FOREIGN KEY (`varaus_id`) REFERENCES `varaus` (`varaus_id`) ON DELETE SET NULL ON UPDATE NO ACTION;

--
-- Constraints for table `varaus`
--
ALTER TABLE `varaus`
  ADD CONSTRAINT `asiakas_id_va` FOREIGN KEY (`asiakas_id`) REFERENCES `asiakas` (`asiakas_id`) ON DELETE SET NULL ON UPDATE NO ACTION,
  ADD CONSTRAINT `mokki_id_va` FOREIGN KEY (`mokki_id`) REFERENCES `mokki` (`mokki_id`) ON DELETE SET NULL ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
