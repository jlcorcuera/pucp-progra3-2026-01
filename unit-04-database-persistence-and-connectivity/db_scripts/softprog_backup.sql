-- MySQL dump 10.13  Distrib 8.0.45, for Linux (x86_64)
--
-- Host: my-softprog-rds.ctjga6kjqpgm.us-east-1.rds.amazonaws.com    Database: softprog
-- ------------------------------------------------------
-- Server version	8.4.8

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `softprog`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `softprog` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `softprog`;

--
-- Table structure for table `area`
--

DROP TABLE IF EXISTS `area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `area` (
  `id_area` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(75) DEFAULT NULL,
  `activa` tinyint DEFAULT NULL,
  PRIMARY KEY (`id_area`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area`
--

LOCK TABLES `area` WRITE;
/*!40000 ALTER TABLE `area` DISABLE KEYS */;
INSERT INTO `area` VALUES (1,'CONTABILIDAD',1),(2,'VENTAS',1),(3,'CONTABILIDAD',1),(4,'FINANZAS',1);
/*!40000 ALTER TABLE `area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cliente`
--

DROP TABLE IF EXISTS `cliente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cliente` (
  `id_cliente` int NOT NULL,
  `linea_credito` decimal(10,2) DEFAULT NULL,
  `categoria` enum('Clasico','VIP','Gold','Platinum') DEFAULT NULL,
  PRIMARY KEY (`id_cliente`),
  CONSTRAINT `cliente_ibfk_1` FOREIGN KEY (`id_cliente`) REFERENCES `persona` (`id_persona`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cliente`
--

LOCK TABLES `cliente` WRITE;
/*!40000 ALTER TABLE `cliente` DISABLE KEYS */;
INSERT INTO `cliente` VALUES (7,1500.50,'Platinum'),(8,2500.00,'Platinum'),(9,1000.00,'Clasico'),(10,2000.00,'Clasico'),(11,2000.00,'VIP'),(12,3000.00,'Platinum'),(13,3700.00,'VIP'),(14,1430.00,'Clasico'),(15,3345.00,'VIP');
/*!40000 ALTER TABLE `cliente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cuenta_usuario`
--

DROP TABLE IF EXISTS `cuenta_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cuenta_usuario` (
  `id_cuenta_usuario` int NOT NULL AUTO_INCREMENT,
  `fid_empleado` int DEFAULT NULL,
  `username` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_cuenta_usuario`),
  UNIQUE KEY `fid_empleado` (`fid_empleado`),
  CONSTRAINT `cuenta_usuario_ibfk_1` FOREIGN KEY (`fid_empleado`) REFERENCES `empleado` (`id_empleado`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cuenta_usuario`
--

LOCK TABLES `cuenta_usuario` WRITE;
/*!40000 ALTER TABLE `cuenta_usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `cuenta_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `empleado`
--

DROP TABLE IF EXISTS `empleado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `empleado` (
  `id_empleado` int NOT NULL,
  `fid_area` int DEFAULT NULL,
  `cargo` varchar(75) DEFAULT NULL,
  `sueldo` decimal(10,2) DEFAULT NULL,
  `activo` tinyint DEFAULT NULL,
  PRIMARY KEY (`id_empleado`),
  KEY `fid_area` (`fid_area`),
  CONSTRAINT `empleado_ibfk_1` FOREIGN KEY (`id_empleado`) REFERENCES `persona` (`id_persona`),
  CONSTRAINT `empleado_ibfk_2` FOREIGN KEY (`fid_area`) REFERENCES `area` (`id_area`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `empleado`
--

LOCK TABLES `empleado` WRITE;
/*!40000 ALTER TABLE `empleado` DISABLE KEYS */;
INSERT INTO `empleado` VALUES (1,2,'VENDEDOR',2300.00,1),(2,3,'JEFE DE VENTAS',1650.00,1),(3,2,'CAJERA',1500.00,1),(4,2,'VENDEDOR',1750.00,1),(5,3,'ANALISTA CONTABLE',2500.00,1),(6,3,'JEFE DE CONTABILIDAD',3200.00,1);
/*!40000 ALTER TABLE `empleado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `linea_orden_venta`
--

DROP TABLE IF EXISTS `linea_orden_venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `linea_orden_venta` (
  `id_linea_orden_venta` int NOT NULL AUTO_INCREMENT,
  `fid_orden_venta` int DEFAULT NULL,
  `fid_producto` int DEFAULT NULL,
  `cantidad` int DEFAULT NULL,
  `subtotal` decimal(10,2) DEFAULT NULL,
  `activa` tinyint DEFAULT NULL,
  PRIMARY KEY (`id_linea_orden_venta`),
  KEY `fid_orden_venta` (`fid_orden_venta`),
  KEY `fid_producto` (`fid_producto`),
  CONSTRAINT `linea_orden_venta_ibfk_1` FOREIGN KEY (`fid_orden_venta`) REFERENCES `orden_venta` (`id_orden_venta`),
  CONSTRAINT `linea_orden_venta_ibfk_2` FOREIGN KEY (`fid_producto`) REFERENCES `producto` (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `linea_orden_venta`
--

LOCK TABLES `linea_orden_venta` WRITE;
/*!40000 ALTER TABLE `linea_orden_venta` DISABLE KEYS */;
INSERT INTO `linea_orden_venta` VALUES (1,1,4,5,30.50,1),(2,1,3,4,64.00,1),(3,2,1,5,13.50,1),(4,2,4,3,18.30,1),(5,3,3,3,48.00,1),(6,3,4,5,30.50,1),(7,4,1,3,8.10,1),(8,4,3,3,48.00,1),(9,4,2,2,11.80,1),(10,5,2,5,29.50,1),(11,6,4,3,18.30,1),(12,6,2,4,23.60,1),(13,7,3,5,80.00,1),(14,7,4,3,18.30,1),(15,7,1,2,5.40,1),(16,8,1,3,8.10,1),(17,8,4,5,30.50,1),(18,9,4,2,12.20,1),(19,9,3,2,32.00,1),(20,10,4,4,24.40,1),(21,11,4,2,12.20,1),(22,11,1,2,5.40,1),(23,11,3,1,16.00,1),(24,12,1,1,2.70,1),(25,12,3,5,80.00,1),(26,13,4,2,12.20,1),(27,14,4,4,24.40,1),(28,15,4,3,18.30,1),(29,15,2,4,23.60,1),(30,15,1,4,10.80,1),(31,16,4,5,30.50,1),(32,17,4,1,6.10,1),(33,18,3,5,80.00,1),(34,19,4,4,24.40,1),(35,19,3,3,48.00,1),(36,20,2,2,11.80,1),(37,20,1,4,10.80,1),(38,21,1,1,2.70,1),(39,22,1,2,5.40,1),(40,22,4,2,12.20,1),(41,22,2,3,17.70,1),(42,23,3,1,16.00,1),(43,23,1,4,10.80,1),(44,23,4,5,30.50,1),(45,24,1,1,2.70,1),(46,24,4,2,12.20,1),(47,25,2,5,29.50,1),(48,25,3,2,32.00,1),(49,25,1,2,5.40,1),(50,26,4,3,18.30,1),(51,26,3,5,80.00,1),(52,27,2,2,11.80,1),(53,28,4,4,24.40,1),(54,29,3,5,80.00,1),(55,29,4,2,12.20,1),(56,29,2,4,23.60,1),(57,30,1,2,5.40,1),(58,30,4,5,30.50,1),(59,30,3,3,48.00,1),(60,31,4,5,30.50,1),(61,31,3,3,48.00,1),(62,31,1,2,5.40,1),(63,32,4,1,6.10,1),(64,32,3,2,32.00,1),(65,32,2,1,5.90,1),(66,33,2,5,29.50,1),(67,33,1,5,13.50,1),(68,33,4,3,18.30,1),(69,34,1,3,8.10,1),(70,35,2,3,17.70,1),(71,35,1,3,8.10,1),(72,35,4,2,12.20,1),(73,36,1,3,8.10,1),(74,36,2,5,29.50,1),(75,36,3,2,32.00,1),(76,37,1,3,8.10,1),(77,37,3,1,16.00,1),(78,37,2,5,29.50,1),(79,38,4,4,24.40,1),(80,39,2,2,11.80,1),(81,39,4,2,12.20,1),(82,40,4,1,6.10,1),(83,40,1,3,8.10,1),(84,41,1,2,5.40,1),(85,41,4,2,12.20,1),(86,42,4,5,30.50,1),(87,42,3,2,32.00,1),(88,42,2,2,11.80,1);
/*!40000 ALTER TABLE `linea_orden_venta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orden_venta`
--

DROP TABLE IF EXISTS `orden_venta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orden_venta` (
  `id_orden_venta` int NOT NULL AUTO_INCREMENT,
  `fid_empleado` int DEFAULT NULL,
  `fid_cliente` int DEFAULT NULL,
  `total` decimal(10,2) DEFAULT NULL,
  `fecha_hora` datetime DEFAULT NULL,
  `activa` tinyint DEFAULT NULL,
  PRIMARY KEY (`id_orden_venta`),
  KEY `fid_empleado` (`fid_empleado`),
  KEY `fid_cliente` (`fid_cliente`),
  CONSTRAINT `orden_venta_ibfk_1` FOREIGN KEY (`fid_empleado`) REFERENCES `empleado` (`id_empleado`),
  CONSTRAINT `orden_venta_ibfk_2` FOREIGN KEY (`fid_cliente`) REFERENCES `cliente` (`id_cliente`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orden_venta`
--

LOCK TABLES `orden_venta` WRITE;
/*!40000 ALTER TABLE `orden_venta` DISABLE KEYS */;
INSERT INTO `orden_venta` VALUES (1,3,11,94.50,'2025-01-10 20:27:41',1),(2,3,8,31.80,'2025-01-05 13:22:50',1),(3,4,10,78.50,'2025-01-25 08:00:23',1),(4,4,9,67.90,'2025-02-03 19:08:07',1),(5,1,8,29.50,'2025-03-07 11:10:10',1),(6,3,10,41.90,'2025-04-03 15:36:33',1),(7,1,8,103.70,'2025-04-27 19:53:20',1),(8,3,10,38.60,'2025-04-02 08:49:21',1),(9,3,11,44.20,'2025-04-01 11:06:30',1),(10,5,10,24.40,'2025-05-02 16:03:30',1),(11,1,10,33.60,'2025-05-05 16:37:08',1),(12,2,7,82.70,'2025-06-05 18:08:57',1),(13,3,10,12.20,'2025-07-11 11:26:12',1),(14,3,10,24.40,'2025-07-06 16:28:31',1),(15,1,7,52.70,'2025-07-14 16:20:33',1),(16,4,9,30.50,'2025-07-27 09:28:45',1),(17,2,8,6.10,'2025-07-07 08:11:35',1),(18,4,7,80.00,'2025-08-14 14:35:14',1),(19,4,7,72.40,'2025-09-05 12:56:56',1),(20,2,7,22.60,'2025-09-04 14:23:28',1),(21,4,11,2.70,'2025-09-06 12:28:31',1),(22,3,10,35.30,'2025-10-04 09:55:31',1),(23,5,11,57.30,'2025-10-11 11:25:21',1),(24,3,10,14.90,'2025-10-15 11:59:43',1),(25,4,8,66.90,'2025-10-02 12:34:24',1),(26,4,9,98.30,'2025-10-07 15:45:36',1),(27,1,11,11.80,'2025-11-28 15:48:46',1),(28,4,8,24.40,'2025-11-21 18:24:38',1),(29,5,7,115.80,'2025-11-01 17:20:20',1),(30,4,9,83.90,'2025-11-09 17:42:00',1),(31,4,10,83.90,'2025-11-27 14:06:52',1),(32,4,7,44.00,'2025-12-04 09:48:49',1),(33,3,9,61.30,'2025-12-19 12:05:22',1),(34,3,10,8.10,'2026-01-01 15:02:50',1),(35,5,10,38.00,'2026-01-11 09:11:58',1),(36,2,7,69.60,'2026-01-05 20:08:07',1),(37,4,10,53.60,'2026-02-04 17:02:16',1),(38,5,7,24.40,'2026-03-29 15:34:50',1),(39,5,11,24.00,'2026-04-12 13:57:28',1),(40,3,7,14.20,'2026-04-06 13:59:12',1),(41,3,9,17.60,'2026-04-13 16:03:18',1),(42,4,10,74.30,'2026-04-19 13:52:11',1);
/*!40000 ALTER TABLE `orden_venta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persona`
--

DROP TABLE IF EXISTS `persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persona` (
  `id_persona` int NOT NULL AUTO_INCREMENT,
  `DNI` varchar(8) DEFAULT NULL,
  `nombre` varchar(70) DEFAULT NULL,
  `apellido_paterno` varchar(70) DEFAULT NULL,
  `sexo` char(1) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  PRIMARY KEY (`id_persona`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persona`
--

LOCK TABLES `persona` WRITE;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
INSERT INTO `persona` VALUES (1,'28761129','MANUEL','GONZALES','M','1986-11-01'),(2,'27519001','KARLA','CORDOVA','F','1993-08-17'),(3,'12987109','KAREN','DIAZ','F','1986-11-01'),(4,'29121803','JUAN','ARENAS','M','1991-02-19'),(5,'17300362','MANUEL','CARRASCO','M','1982-07-18'),(6,'18762501','KAREN','MARTINEZ','F','1976-01-14'),(7,'87261109','FATIMA','MORALES','F','1992-10-04'),(8,'13007065','CAROLINA','SALVADOR','F','1992-11-22'),(9,'18732004','DANIELA','VILLANUEVA','F','1984-03-15'),(10,'39871002','HUGO','VALDIVIA','M','1992-03-03'),(11,'28709982','OSCAR','CARRANZA','M','1993-10-30'),(12,'10920091','ANGELA','GUEVARA','F','1988-03-13'),(13,'33620929','PEDRO','MENDOZA','M','1984-10-09'),(14,'17200928','CARMEN','GAVIDIA','F','1981-02-15'),(15,'28779283','PIERINA','RUIZ','F','1984-11-23');
/*!40000 ALTER TABLE `persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `producto`
--

DROP TABLE IF EXISTS `producto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `producto` (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) DEFAULT NULL,
  `unidad_medida` varchar(75) DEFAULT NULL,
  `precio` decimal(10,2) DEFAULT NULL,
  `activo` tinyint DEFAULT NULL,
  `stock` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id_producto`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `producto`
--

LOCK TABLES `producto` WRITE;
/*!40000 ALTER TABLE `producto` DISABLE KEYS */;
INSERT INTO `producto` VALUES (1,'GASEOSA INKA KOLA','500 ML',2.70,1,142),(2,'GASEOSA COCA COLA','1.5 LT',5.90,1,142),(3,'DETERGENTE LIQUIDO BOLIVAR','940 ML',16.00,1,135),(4,'LAVAVAJILLAS EN PASTA LIMON SAPOLIO','900 GR',6.10,1,148);
/*!40000 ALTER TABLE `producto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'softprog'
--
/*!50003 DROP PROCEDURE IF EXISTS `ELIMINAR_AREA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `ELIMINAR_AREA`(
	IN _id_area INT
)
BEGIN
	UPDATE area SET activa = 0 WHERE id_area = _id_area;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ELIMINAR_EMPLEADO` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `ELIMINAR_EMPLEADO`(
	IN _id_empleado INT
)
BEGIN
    UPDATE empleado SET activo = 0 WHERE id_empleado = _id_empleado;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `ELIMINAR_ORDEN_VENTA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `ELIMINAR_ORDEN_VENTA`(
	IN _id_orden_venta INT
)
BEGIN
	UPDATE orden_venta SET activa = 0 WHERE id_orden_venta = _id_orden_venta;
    UPDATE linea_orden_venta SET activa = 0 WHERE fid_orden_venta = _id_orden_venta;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `INSERTAR_AREA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `INSERTAR_AREA`(
	OUT _id_area INT,
    IN _nombre VARCHAR(75)
)
BEGIN
	INSERT INTO area(nombre,activa) VALUES(_nombre,1);
    SET _id_area = @@last_insert_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `INSERTAR_CLIENTE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `INSERTAR_CLIENTE`(
	OUT _id_cliente INT,
	IN _DNI VARCHAR(8),
    IN _nombre VARCHAR(70),
    IN _apellido_paterno VARCHAR(70),
    IN _sexo CHAR,
    IN _fecha_nacimiento DATE,
    IN _linea_credito DECIMAL(10,2),
    IN _categoria ENUM('Clasico','VIP','Gold','Platinum')
)
BEGIN
	INSERT INTO persona(DNI,nombre,apellido_paterno,sexo,fecha_nacimiento) VALUES(_DNI,_nombre,_apellido_paterno,_sexo,_fecha_nacimiento);
	SET _id_cliente = @@last_insert_id;
	INSERT INTO cliente(id_cliente,linea_credito,categoria) VALUES(_id_cliente,_linea_credito,_categoria);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `INSERTAR_EMPLEADO` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `INSERTAR_EMPLEADO`(
	OUT _id_empleado INT,
    IN _fid_area INT,
    IN _DNI VARCHAR(8),
    IN _nombre VARCHAR(70),
    IN _apellido_paterno VARCHAR(70),
    IN _sexo CHAR,
    IN _fecha_nacimiento DATE,
    IN _cargo VARCHAR(75),
    IN _sueldo DECIMAL(10,2)
)
BEGIN
	INSERT INTO persona(DNI,nombre,apellido_paterno,sexo,fecha_nacimiento) VALUES(_DNI,_nombre,_apellido_paterno,_sexo,_fecha_nacimiento);
    SET _id_empleado = @@last_insert_id;
    INSERT INTO empleado(id_empleado,fid_area,cargo,sueldo,activo) VALUES(_id_empleado,_fid_area,_cargo,_sueldo,1);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `INSERTAR_LINEA_ORDEN_VENTA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `INSERTAR_LINEA_ORDEN_VENTA`(
	OUT _id_linea_orden_venta INT,
    IN _fid_orden_venta INT,
    IN _fid_producto INT,
    IN _cantidad INT,
    IN _subtotal DECIMAL(10,2)
)
BEGIN
	INSERT INTO linea_orden_venta(fid_orden_venta,fid_producto,cantidad,subtotal,activa) VALUES(_fid_orden_venta,_fid_producto,_cantidad,_subtotal,1);
    SET _id_linea_orden_venta = @@last_insert_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `INSERTAR_ORDEN_VENTA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `INSERTAR_ORDEN_VENTA`(
	OUT _id_orden_venta INT,
    IN _fid_empleado INT,
    IN _fid_cliente INT,
    IN _total DECIMAL(10,2)
)
BEGIN
	INSERT INTO orden_venta(fid_empleado,fid_cliente,total,fecha_hora,activa) VALUES(_fid_empleado,_fid_cliente,_total,now() - INTERVAL 5 HOUR,1);
    SET _id_orden_venta = @@last_insert_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `INSERTAR_PRODUCTO` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `INSERTAR_PRODUCTO`(
	OUT _id_producto INT,
    IN _nombre VARCHAR(100),
    IN _unidad_medida VARCHAR(75),
    IN _precio DECIMAL(10,2)
)
BEGIN
	INSERT INTO producto(nombre,unidad_medida,precio,activo) VALUES(_nombre,_unidad_medida,_precio,1);
    SET _id_producto = @@last_insert_id;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `LISTAR_AREAS_TODAS` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `LISTAR_AREAS_TODAS`()
BEGIN
	SELECT id_area, nombre, activa FROM area WHERE activa = 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `LISTAR_CLIENTES_TODOS` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `LISTAR_CLIENTES_TODOS`()
BEGIN
	SELECT c.id_cliente, p.DNI, p.nombre, p.apellido_paterno, p.sexo, p.fecha_nacimiento, c.linea_credito, c.categoria FROM persona p INNER JOIN cliente c ON p.id_persona = c.id_cliente;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `LISTAR_CLIENTES_X_DNI_NOMBRE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `LISTAR_CLIENTES_X_DNI_NOMBRE`(
	IN _DNI_nombre VARCHAR(140)
)
BEGIN
	SELECT c.id_cliente, p.DNI, p.nombre, p.apellido_paterno, p.sexo, p.fecha_nacimiento, c.linea_credito, c.categoria FROM persona p INNER JOIN cliente c ON p.id_persona = c.id_cliente AND ((p.DNI LIKE CONCAT('%',_DNI_nombre,'%')) OR (CONCAT(p.nombre,' ',p.apellido_paterno) LIKE CONCAT('%',_DNI_nombre,'%')));
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `LISTAR_EMPLEADOS_TODOS` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `LISTAR_EMPLEADOS_TODOS`()
BEGIN
	SELECT e.id_empleado, p.DNI, p.nombre, p.apellido_paterno, p.sexo, p.fecha_nacimiento, a.id_area, a.nombre as nombre_area, e.cargo, e.sueldo FROM persona p INNER JOIN empleado e ON p.id_persona = e.id_empleado INNER JOIN area a ON e.fid_area = a.id_area WHERE e.activo = 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `LISTAR_LINEAS_ORDEN_VENTA_X_ID_ORDEN_VENTA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `LISTAR_LINEAS_ORDEN_VENTA_X_ID_ORDEN_VENTA`(
	IN _id_orden_venta INT
)
BEGIN
	SELECT lov.id_linea_orden_venta, lov.fid_orden_venta, p.id_producto, p.nombre, p.precio, p.unidad_medida, lov.cantidad, lov.subtotal FROM linea_orden_venta lov INNER JOIN producto p ON lov.fid_producto = p.id_producto WHERE lov.fid_orden_venta = _id_orden_venta AND activa = 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `LISTAR_PRODUCTOS_TODOS` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `LISTAR_PRODUCTOS_TODOS`()
BEGIN
	SELECT id_producto, nombre, unidad_medida, precio FROM producto WHERE activo = 1;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `LISTAR_PRODUCTOS_X_NOMBRE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `LISTAR_PRODUCTOS_X_NOMBRE`(
	IN _nombre VARCHAR(100)
)
BEGIN
	SELECT id_producto, nombre, unidad_medida, precio FROM producto WHERE activo = 1 AND nombre LIKE CONCAT('%',_nombre,'%');
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `MODIFICAR_AREA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `MODIFICAR_AREA`(
	IN _id_area INT,
    IN _nombre VARCHAR(75)
)
BEGIN
	UPDATE area SET nombre = _nombre WHERE id_area = _id_area;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `MODIFICAR_CLIENTE` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `MODIFICAR_CLIENTE`(
	IN _id_cliente INT,
	IN _DNI VARCHAR(8),
    IN _nombre VARCHAR(70),
    IN _apellido_paterno VARCHAR(70),
    IN _sexo CHAR,
    IN _fecha_nacimiento DATE,
    IN _linea_credito DECIMAL(10,2),
    IN _categoria ENUM('Clasico','VIP','Gold','Platinum')
)
BEGIN
	UPDATE persona SET DNI = _DNI, nombre = _nombre, apellido_paterno = _apellido_paterno,sexo = _sexo, fecha_nacimiento = _fecha_nacimiento WHERE id_persona = _id_cliente;
	UPDATE cliente SET linea_credito = _linea_credito, categoria = _categoria WHERE id_cliente = _id_cliente;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `MODIFICAR_EMPLEADO` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `MODIFICAR_EMPLEADO`(
	IN _id_empleado INT,
    IN _fid_area INT,
    IN _DNI VARCHAR(8),
    IN _nombre VARCHAR(70),
    IN _apellido_paterno VARCHAR(70),
    IN _sexo CHAR,
    IN _fecha_nacimiento DATE,
    IN _cargo VARCHAR(75),
    IN _sueldo DECIMAL(10,2)
)
BEGIN
	UPDATE persona SET DNI = _DNI, nombre = _nombre, apellido_paterno = _apellido_paterno, sexo = _sexo, fecha_nacimiento = _fecha_nacimiento WHERE id_persona = _id_empleado;
    UPDATE empleado SET fid_area = _fid_area, cargo = _cargo, sueldo = _sueldo WHERE id_empleado = _id_empleado;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `MODIFICAR_ORDEN_VENTA` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `MODIFICAR_ORDEN_VENTA`(
	IN _id_orden_venta INT,
    IN _fid_empleado INT,
    IN _fid_cliente INT,
    IN _total DECIMAL(10,2)
)
BEGIN
	UPDATE linea_orden_venta SET activa = 0 WHERE fid_orden_venta = _id_orden_venta;
	UPDATE orden_venta SET fid_empleado = _fid_empleado,fid_cliente = _fid_cliente, total = _total, fecha_hora = now() - INTERVAL 5 HOUR WHERE id_orden_venta = _id_orden_venta;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `OBTENER_AREA_X_ID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `OBTENER_AREA_X_ID`(
	IN _id_area INT
)
BEGIN
	SELECT id_area, nombre, activa FROM area WHERE id_area = _id_area;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `OBTENER_CLIENTE_X_ID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `OBTENER_CLIENTE_X_ID`(
	IN _id_cliente INT
)
BEGIN
	SELECT c.id_cliente, p.DNI, p.nombre, p.apellido_paterno, p.sexo, p.fecha_nacimiento, c.linea_credito, c.categoria FROM persona p INNER JOIN cliente c ON p.id_persona = c.id_cliente WHERE c.id_cliente = _id_cliente;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `OBTENER_EMPLEADO_X_ID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `OBTENER_EMPLEADO_X_ID`(
	IN _id_empleado INT
)
BEGIN
	SELECT e.id_empleado, p.DNI, p.nombre, p.apellido_paterno, p.sexo, p.fecha_nacimiento, a.id_area, a.nombre as nombre_area, e.cargo, e.sueldo FROM persona p INNER JOIN empleado e ON p.id_persona = e.id_empleado INNER JOIN area a ON e.fid_area = a.id_area WHERE e.id_empleado = _id_empleado;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `OBTENER_ORDEN_VENTA_X_ID` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `OBTENER_ORDEN_VENTA_X_ID`(
	IN _id_orden_venta INT
)
BEGIN
	SELECT ov.id_orden_venta, e.id_empleado, p1.DNI as dni_empleado, p1.nombre as nombre_empleado, p1.apellido_paterno as apellido_paterno_empleado, p1.fecha_nacimiento as fecha_nacimiento_empleado, p1.sexo as sexo_empleado, e.cargo as cargo_empleado, a.id_area, a.nombre as nombre_area, e.sueldo as sueldo_empleado, c.id_cliente, p2.DNI as dni_cliente, p2.nombre as nombre_cliente, p2.apellido_paterno as apellido_paterno_cliente, p2.fecha_nacimiento as fecha_nacimiento_cliente, p2.sexo as sexo_cliente, c.categoria as categoria_cliente, c.linea_credito as linea_credito_cliente, ov.total, ov.fecha_hora FROM orden_venta ov INNER JOIN empleado e ON ov.fid_empleado = e.id_empleado INNER JOIN persona p1 ON p1.id_persona = e.id_empleado INNER JOIN area a ON a.id_area = e.fid_area INNER JOIN cliente c ON c.id_cliente = ov.fid_cliente INNER JOIN persona p2 ON p2.id_persona = c.id_cliente WHERE ov.id_orden_venta = _id_orden_venta;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VERIFICAR_CUENTA_USUARIO` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`admin`@`%` PROCEDURE `VERIFICAR_CUENTA_USUARIO`(
	IN _username VARCHAR(100),
    IN _password VARCHAR(100)
)
BEGIN
	SELECT * FROM cuenta_usuario WHERE username  = _username AND password = MD5(_password);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-19 21:24:02
