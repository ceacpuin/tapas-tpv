--------------------------------------------------------------------------------
-- Copyright (C) 2010 Francisco José Morero Peyrona. All Rights Reserved.
--
-- This file is part of Tapas project: http://code.google.com/p/tapas-tpv/
--
-- GNU Classpath is free software; you can redistribute it and/or modify it
-- under the terms of the GNU General Public License as published by the free
-- Software Foundation; either version 3, or (at your option) any later version.
--
-- Tapas is distributed in the hope that it will be useful, but WITHOUT ANY
-- WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
-- A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License along with
-- Tapas; see the file COPYING.  If not, write to the Free Software Foundation,
-- Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
--------------------------------------------------------------------------------

-- *****************************************************************************
--   TAPAS - RELATIONAL DATA MODEL DEFINITION FOR APACHE DERBY DATABASE
--------------------------------------------------------------------------------
--   To access data base:
--      user     = admin
--      password = admin
-- *****************************************************************************

-- -----------------------------------------------------------------------------
-- TABLES DEFINITION
-- -----------------------------------------------------------------------------

CREATE TABLE APP.ventas(
   PRIMARY KEY (id_venta),
   id_venta    INTEGER      GENERATED ALWAYS AS IDENTITY,
   cliente     VARCHAR(32),
   modo_pago   INTEGER      NOT NULL,
   cuando      TIMESTAMP    NOT NULL,
   total       NUMERIC(6,2) NOT NULL );

CREATE TABLE APP.ventas_detalle(
   PRIMARY KEY (id_ventas_detalle),
   id_ventas_detalle INTEGER      GENERATED ALWAYS AS IDENTITY,
   id_venta          INTEGER      NOT NULL,
   cantidad          INTEGER      NOT NULL,
   producto          VARCHAR(32)  NOT NULL,
   precio            NUMERIC(6,2) NOT NULL);

CREATE TABLE APP.categorias(
   PRIMARY KEY (id_categoria),
   id_categoria INTEGER     GENERATED ALWAYS AS IDENTITY,
   nombre       VARCHAR(16) NOT NULL,
   icono        BLOB );

CREATE TABLE APP.productos(
   PRIMARY KEY (id_producto),
   id_producto    INTEGER      GENERATED ALWAYS AS IDENTITY,
   id_categoria   INTEGER      NOT NULL,
   nombre         VARCHAR(16)  NOT NULL,
   descripcion    VARCHAR(32)  NOT NULL,
   precio         NUMERIC(6,2) NOT NULL,
   icono          BLOB );

CREATE TABLE APP.configuracion(
   id_configuracion INTEGER     GENERATED ALWAYS AS IDENTITY,
   contrasena       VARCHAR( 32),
   email            VARCHAR( 48),
   carpeta_musica   VARCHAR(256),
   full_screen      INTEGER,
   auto_alinear     INTEGER,
   ticket_imagen    BLOB,
   ticket_cabecera  VARCHAR(999),
   ticket_pie       VARCHAR(999) );

-- -----------------------------------------------------------------------------
-- FOREING KEYS
-- -----------------------------------------------------------------------------

ALTER TABLE APP.ventas_detalle
     ADD CONSTRAINT fk_ventas_detalle
         FOREIGN KEY (id_venta)
         REFERENCES APP.ventas (id_venta)
         ON DELETE CASCADE;

ALTER TABLE APP.productos
     ADD CONSTRAINT fk_categorias_productos
         FOREIGN KEY (id_categoria)
         REFERENCES APP.categorias (id_categoria)
         ON DELETE CASCADE;

-- -----------------------------------------------------------------------------
-- INDEXES
-- -----------------------------------------------------------------------------

CREATE INDEX ventas_modo_pago ON APP.ventas (modo_pago);
CREATE INDEX ventas_cuando    ON APP.ventas (cuando);

-- -----------------------------------------------------------------------------
-- INITIAL DATA
-- -----------------------------------------------------------------------------

INSERT INTO APP.configuracion (auto_alinear) VALUES ( 1 );

-- ********************************   EOF  *************************************