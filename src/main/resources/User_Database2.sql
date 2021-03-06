-- MySQL Script generated by MySQL Workbench
-- Wed Mar 11 12:05:00 2020
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';
CREATE SCHEMA IF NOT EXISTS `User_Database` DEFAULT CHARACTER SET utf8 ;
USE `User_Database` ;
-- -----------------------------------------------------
-- Table 'user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `users` (
    `UserID` INT NOT NULL,
    `Username` VARCHAR(20) NULL,
    `Ini` VARCHAR(4) NULL,
    `CPR` VARCHAR(11) NULL,
    `Password` VARCHAR(100) NULL,
    `Role` ENUM('Administrator', 'Farmaceut', 'Formand', 'Operatør') NULL,
    PRIMARY KEY (`UserID`));
SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
