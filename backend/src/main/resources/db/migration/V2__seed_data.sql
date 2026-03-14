-- =============================================
-- V2: Seed data — 4 tenants with varying rates
-- =============================================

INSERT INTO tenants (name, domain, status, commission_rate) VALUES
    ('HappyPaws',    'happypaws.com',    'ACTIVE', 10.00),
    ('TechGear',     'techgear.io',      'ACTIVE', 12.50),
    ('FitLife',      'fitlife.co',       'ACTIVE',  8.00),
    ('BookNest',     'booknest.com',     'ACTIVE', 15.00);
