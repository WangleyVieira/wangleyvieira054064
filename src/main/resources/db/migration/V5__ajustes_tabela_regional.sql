--  Criar sequence (se não existir)
CREATE SEQUENCE IF NOT EXISTS regional_id_seq;

-- Ajustar a coluna id para usar a sequence
ALTER TABLE regional
    ALTER COLUMN id SET DEFAULT nextval('regional_id_seq');

-- Garantir que a sequence esteja sincronizada
SELECT setval(
       'regional_id_seq',
       COALESCE((SELECT MAX(id) FROM regional), 1)
);

ALTER TABLE regional
    ADD COLUMN codigo_externo INTEGER;

-- Criar índice
CREATE INDEX idx_regional_codigo_externo
    ON regional (codigo_externo);


