-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema CA1
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `CA1` ;

-- -----------------------------------------------------
-- Schema CA1
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `CA1` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `CA1` ;

-- -----------------------------------------------------
-- Table `CA1`.`city_info`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CA1`.`city_info` ;

CREATE TABLE IF NOT EXISTS `CA1`.`city_info` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `zipcode` VARCHAR(45) NOT NULL,
  `city` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `CA1`.`address`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CA1`.`address` ;

CREATE TABLE IF NOT EXISTS `CA1`.`address` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `street` VARCHAR(45) NOT NULL,
  `additional_info` VARCHAR(45) NOT NULL,
  `city_info_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_address_city_info1_idx` (`city_info_id` ASC) VISIBLE,
  CONSTRAINT `fk_address_city_info1`
    FOREIGN KEY (`city_info_id`)
    REFERENCES `CA1`.`city_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `CA1`.`hobby`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CA1`.`hobby` ;

CREATE TABLE IF NOT EXISTS `CA1`.`hobby` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `CA1`.`person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CA1`.`person` ;

CREATE TABLE IF NOT EXISTS `CA1`.`person` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `address_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_person_address1_idx` (`address_id` ASC) VISIBLE,
  CONSTRAINT `fk_person_address1`
    FOREIGN KEY (`address_id`)
    REFERENCES `CA1`.`address` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `CA1`.`hobby_has_person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CA1`.`hobby_has_person` ;

CREATE TABLE IF NOT EXISTS `CA1`.`hobby_has_person` (
  `hobby_id` INT NOT NULL,
  `person_id` INT NOT NULL,
  PRIMARY KEY (`hobby_id`, `person_id`),
  INDEX `fk_hobby_has_person_person1_idx` (`person_id` ASC) VISIBLE,
  INDEX `fk_hobby_has_person_hobby1_idx` (`hobby_id` ASC) VISIBLE,
  CONSTRAINT `fk_hobby_has_person_hobby1`
    FOREIGN KEY (`hobby_id`)
    REFERENCES `CA1`.`hobby` (`id`),
  CONSTRAINT `fk_hobby_has_person_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `CA1`.`person` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `CA1`.`phone`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `CA1`.`phone` ;

CREATE TABLE IF NOT EXISTS `CA1`.`phone` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `number` INT NOT NULL,
  `description` VARCHAR(45) NOT NULL,
  `person_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_phone_person1_idx` (`person_id` ASC) VISIBLE,
  CONSTRAINT `fk_phone_person1`
    FOREIGN KEY (`person_id`)
    REFERENCES `CA1`.`person` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
