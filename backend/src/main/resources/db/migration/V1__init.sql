-- =============================================
-- RunLoyal Multi-Tenant Affiliate Platform
-- V1: Initial schema — 7 tables + indexes
-- =============================================

-- 1. Tenants
CREATE TABLE tenants (
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    domain        VARCHAR(255),
    status        VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE',
    commission_rate DECIMAL(5,2) NOT NULL DEFAULT 10.00,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW()
);

-- 2. Affiliates
CREATE TABLE affiliates (
    id         BIGSERIAL PRIMARY KEY,
    tenant_id  BIGINT       NOT NULL REFERENCES tenants(id),
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    status     VARCHAR(50)  NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_affiliates_tenant ON affiliates(tenant_id);

-- 3. Items
CREATE TABLE items (
    id         BIGSERIAL PRIMARY KEY,
    tenant_id  BIGINT       NOT NULL REFERENCES tenants(id),
    name       VARCHAR(255) NOT NULL,
    type       VARCHAR(50)  NOT NULL,
    category   VARCHAR(255),
    price      DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP    NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_items_tenant ON items(tenant_id);

-- 4. Campaigns
CREATE TABLE campaigns (
    id          BIGSERIAL PRIMARY KEY,
    tenant_id   BIGINT       NOT NULL REFERENCES tenants(id),
    name        VARCHAR(255) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_campaigns_tenant ON campaigns(tenant_id);

-- 5. Affiliate Links
CREATE TABLE affiliate_links (
    id            BIGSERIAL PRIMARY KEY,
    tenant_id     BIGINT       NOT NULL REFERENCES tenants(id),
    affiliate_id  BIGINT       NOT NULL REFERENCES affiliates(id),
    item_id       BIGINT       NOT NULL REFERENCES items(id),
    campaign_id   BIGINT       NOT NULL REFERENCES campaigns(id),
    short_code    VARCHAR(20)  NOT NULL,
    tracking_code VARCHAR(255) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_short_code UNIQUE (short_code)
);
CREATE INDEX idx_affiliate_links_tenant ON affiliate_links(tenant_id);

-- 6. Clicks
CREATE TABLE clicks (
    id           BIGSERIAL PRIMARY KEY,
    tenant_id    BIGINT       NOT NULL REFERENCES tenants(id),
    affiliate_id BIGINT       NOT NULL REFERENCES affiliates(id),
    item_id      BIGINT       REFERENCES items(id),
    campaign_id  BIGINT       REFERENCES campaigns(id),
    ip_address   VARCHAR(50),
    user_agent   TEXT,
    created_at   TIMESTAMP    NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_clicks_tenant_created ON clicks(tenant_id, created_at DESC);

-- 7. Orders
CREATE TABLE orders (
    id           BIGSERIAL PRIMARY KEY,
    tenant_id    BIGINT        NOT NULL REFERENCES tenants(id),
    affiliate_id BIGINT        NOT NULL REFERENCES affiliates(id),
    item_id      BIGINT        REFERENCES items(id),
    campaign_id  BIGINT        REFERENCES campaigns(id),
    order_value  DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    commission   DECIMAL(12,2) NOT NULL DEFAULT 0.00,
    created_at   TIMESTAMP     NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_orders_tenant_created ON orders(tenant_id, created_at DESC);
