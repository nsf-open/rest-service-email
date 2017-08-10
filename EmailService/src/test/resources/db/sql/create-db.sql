CREATE SCHEMA IF NOT EXISTS dbo;
SET SCHEMA dbo;

    CREATE TABLE IF NOT EXISTS
    ntfy_ltr
    (
        ntfy_ltr_id NUMERIC(10,0) NOT NULL IDENTITY,
        ltr_cntn_txt TEXT NULL,
        ltr_subj VARCHAR(512) NULL,
        ltr_stts_code CHAR(2) NOT NULL,
        ltr_stts_date DATETIME NOT NULL,
        ltr_stts_user_id CHAR(8) NOT NULL,
        plain_txt_fmt_flag CHAR(1) NULL,
        tmpl_id NUMERIC(10,0) NULL,
        appl_id NUMERIC(10,0) NOT NULL,
        last_updt_pgm CHAR(8) NOT NULL,
        last_updt_user CHAR(8) NOT NULL,
        last_updt_tmsp DATETIME NOT NULL
    );

    CREATE TABLE IF NOT EXISTS
    ntfy_ltr_recp
    (
        ntfy_ltr_id NUMERIC(10,0) NOT NULL,
        ltr_recp_type_code CHAR(2) NOT NULL,
        emai_addr VARCHAR(255) NULL,
        last_updt_pgm CHAR(8) NOT NULL,
        last_updt_user CHAR(8) NOT NULL,
        last_updt_tmsp DATETIME NOT NULL
    );

    CREATE TABLE IF NOT EXISTS
    ntfy_ltr_addl_info
    (
        ntfy_ltr_id NUMERIC(10,0) NOT NULL,
        ntfy_ltr_atr_id NUMERIC(10,0) NOT NULL,
        atr_val VARCHAR(512) NOT NULL,
        last_updt_pgm CHAR(8) NOT NULL,
        last_updt_user CHAR(8) NOT NULL,
        last_updt_tmsp DATETIME NOT NULL
    );

    CREATE TABLE IF NOT EXISTS
    ntfy_ltr_atr_lkup
    (
        ntfy_ltr_atr_id NUMERIC(10,0) NOT NULL,
        atr_name VARCHAR(512) NOT NULL,
        eff_date DATETIME NOT NULL,
        end_date DATETIME NOT NULL,
        last_updt_pgm CHAR(8) NOT NULL,
        last_updt_user CHAR(8) NOT NULL,
        last_updt_tmsp DATETIME NOT NULL
    );


 



