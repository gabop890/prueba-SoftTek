-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1:3306
-- Tiempo de generación: 14-02-2023 a las 00:21:22
-- Versión del servidor: 5.7.31
-- Versión de PHP: 7.3.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `softtek`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `conductor`
--

DROP TABLE IF EXISTS `conductor`;
CREATE TABLE IF NOT EXISTS `conductor` (
  `numiden` int(11) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  PRIMARY KEY (`numiden`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `conductor`
--

INSERT INTO `conductor` (`numiden`, `nombre`) VALUES
(12, 'mario'),
(12345, 'pedro');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `manejo`
--

DROP TABLE IF EXISTS `manejo`;
CREATE TABLE IF NOT EXISTS `manejo` (
  `placa` varchar(6) NOT NULL,
  `conductor` int(11) NOT NULL,
  `fechaini` date NOT NULL,
  `fechafin` date NOT NULL,
  PRIMARY KEY (`placa`,`conductor`),
  KEY `conductor` (`conductor`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `proveedor`
--

DROP TABLE IF EXISTS `proveedor`;
CREATE TABLE IF NOT EXISTS `proveedor` (
  `numiden` bigint(20) NOT NULL,
  `nombre` varchar(20) NOT NULL,
  `direccion` varchar(20) NOT NULL,
  `correo` varchar(30) NOT NULL,
  PRIMARY KEY (`numiden`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `proveedor`
--

INSERT INTO `proveedor` (`numiden`, `nombre`, `direccion`, `correo`) VALUES
(567, 'ath', 'cra. 1 #65-96', 'crftvgyhujnk'),
(1356, 'coordinadora', 'cra.85 # 63-51', 'correo@coordinadora.com'),
(5678, 'tcc', 'cra. 90 45-85', 'tcc@google.com'),
(12345, 'servientrega', 'cl. 45 # 56-56', 'servientrega@as.co');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `vehiculo`
--

DROP TABLE IF EXISTS `vehiculo`;
CREATE TABLE IF NOT EXISTS `vehiculo` (
  `placa` varchar(6) NOT NULL,
  `marca` varchar(20) NOT NULL,
  `modelo` int(4) NOT NULL,
  `estado` varchar(5) NOT NULL,
  `proveedor` bigint(20) NOT NULL,
  PRIMARY KEY (`placa`),
  KEY `proveedor` (`proveedor`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Volcado de datos para la tabla `vehiculo`
--

INSERT INTO `vehiculo` (`placa`, `marca`, `modelo`, `estado`, `proveedor`) VALUES
('bnm987', 'shana', 2017, 'ac', 567),
('fgh687', 'ford', 2014, 'd', 5678),
('ghj678', 'daf', 2015, 'a', 1356),
('kjh987', 'kia', 2023, 'ac', 5678),
('qwe123', 'daf', 2023, 'dis', 567);

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `manejo`
--
ALTER TABLE `manejo`
  ADD CONSTRAINT `manejo_ibfk_1` FOREIGN KEY (`conductor`) REFERENCES `conductor` (`numiden`),
  ADD CONSTRAINT `manejo_ibfk_2` FOREIGN KEY (`placa`) REFERENCES `vehiculo` (`placa`);

--
-- Filtros para la tabla `vehiculo`
--
ALTER TABLE `vehiculo`
  ADD CONSTRAINT `vehiculo_ibfk_1` FOREIGN KEY (`proveedor`) REFERENCES `proveedor` (`numiden`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
