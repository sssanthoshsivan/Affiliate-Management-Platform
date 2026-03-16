-- =============================================
-- V3: Comprehensive Sample Data (Robust ID Discovery)
-- =============================================

-- 1. Affiliates
INSERT INTO affiliates (tenant_id, name, email, status)
SELECT t.id, v.name, v.email, v.status
FROM (VALUES
    ('HappyPaws', 'John Barker',       'john@paws-affiliates.com', 'ACTIVE'),
    ('HappyPaws', 'Sarah Whiskers',   'sarah@paws-affiliates.com', 'ACTIVE'),
    ('TechGear',  'Tech Reviewer Pro', 'reviews@techgear-aff.io',  'ACTIVE'),
    ('TechGear',  'Gadget Guru',       'guru@techgear-aff.io',     'ACTIVE'),
    ('FitLife',   'Healthy Habit',     'info@fitlife-aff.co',      'ACTIVE'),
    ('BookNest',  'Book Worm',         'reader@booknest-aff.com',  'ACTIVE')
) AS v(tenant_name, name, email, status)
JOIN tenants t ON t.name = v.tenant_name;

-- 2. Items
INSERT INTO items (tenant_id, name, type, category, price)
SELECT t.id, v.name, v.type, v.category, v.price
FROM (VALUES
    ('HappyPaws', 'Premium Dog Food',          'PRODUCT', 'Pet Supplies',    45.99),
    ('HappyPaws', 'Cat Grooming Kit',          'PRODUCT', 'Pet Accessories', 29.99),
    ('TechGear',  'Ultra Wireless Mouse',      'PRODUCT', 'Electronics',     59.90),
    ('TechGear',  'Mechanical Keyboard',       'PRODUCT', 'Electronics',    120.00),
    ('FitLife',   'Yoga Membership (Monthly)', 'SERVICE', 'Fitness',         50.00),
    ('FitLife',   'Protein Powder',            'PRODUCT', 'Nutrition',       35.00),
    ('BookNest',  'The Great Novel',           'PRODUCT', 'Books',           19.99)
) AS v(tenant_name, name, type, category, price)
JOIN tenants t ON t.name = v.tenant_name;

-- 3. Campaigns
INSERT INTO campaigns (tenant_id, name, description)
SELECT t.id, v.name, v.description
FROM (VALUES
    ('HappyPaws', 'Spring Paws Sale',          'Spring promotion for pet owners'),
    ('TechGear',  'Summer Tech Expo',          'Latest gadgets for summer'),
    ('FitLife',   'New Year, New You',         'Fitness resolution campaign'),
    ('BookNest',  'Reader Appreciation Month', 'Special discounts for avid readers')
) AS v(tenant_name, name, description)
JOIN tenants t ON t.name = v.tenant_name;

-- 4. Affiliate Links
INSERT INTO affiliate_links (tenant_id, affiliate_id, item_id, campaign_id, short_code, tracking_code)
SELECT t.id, a.id, i.id, c.id, v.short_code, v.tracking_code
FROM (VALUES
    ('HappyPaws', 'John Barker', 'Premium Dog Food', 'Spring Paws Sale', 'hp-dog-jb', 'track_paws_dog_john'),
    ('HappyPaws', 'Sarah Whiskers', 'Cat Grooming Kit', 'Spring Paws Sale', 'hp-cat-sw', 'track_paws_cat_sarah'),
    ('TechGear', 'Tech Reviewer Pro', 'Ultra Wireless Mouse', 'Summer Tech Expo', 'tg-mouse-trp', 'track_tech_mouse_pro'),
    ('TechGear', 'Gadget Guru', 'Mechanical Keyboard', 'Summer Tech Expo', 'tg-kbd-gg', 'track_tech_kbd_guru'),
    ('FitLife', 'Healthy Habit', 'Yoga Membership (Monthly)', 'New Year, New You', 'fl-yoga-hh', 'track_fit_yoga_habit'),
    ('BookNest', 'Book Worm', 'The Great Novel', 'Reader Appreciation Month', 'bn-book-bw', 'track_book_novel_worm')
) AS v(t_name, a_name, i_name, c_name, short_code, tracking_code)
JOIN tenants t ON t.name = v.t_name
JOIN affiliates a ON a.name = v.a_name AND a.tenant_id = t.id
JOIN items i ON i.name = v.i_name AND i.tenant_id = t.id
JOIN campaigns c ON c.name = v.c_name AND c.tenant_id = t.id;

-- 5. Clicks
INSERT INTO clicks (tenant_id, affiliate_id, item_id, campaign_id, ip_address, user_agent, created_at)
SELECT t.id, a.id, i.id, c.id, v.ip, v.ua, NOW() - v.offset_interval
FROM (VALUES
    ('HappyPaws', 'John Barker', 'Premium Dog Food', 'Spring Paws Sale', '192.168.1.1', 'Mozilla/5.0...', INTERVAL '1 day'),
    ('HappyPaws', 'John Barker', 'Premium Dog Food', 'Spring Paws Sale', '192.168.1.2', 'Mozilla/5.0...', INTERVAL '36 hours'),
    ('HappyPaws', 'Sarah Whiskers', 'Cat Grooming Kit', 'Spring Paws Sale', '192.168.1.3', 'Mozilla/5.0...', INTERVAL '2 days'),
    ('TechGear', 'Tech Reviewer Pro', 'Ultra Wireless Mouse', 'Summer Tech Expo', '192.168.1.4', 'Mozilla/5.0...', INTERVAL '1 day'),
    ('TechGear', 'Gadget Guru', 'Mechanical Keyboard', 'Summer Tech Expo', '192.168.1.5', 'Mozilla/5.0...', INTERVAL '2 days'),
    ('FitLife', 'Healthy Habit', 'Yoga Membership (Monthly)', 'New Year, New You', '192.168.1.6', 'Mozilla/5.0...', INTERVAL '5 days'),
    ('FitLife', 'Healthy Habit', 'Protein Powder', 'New Year, New You', '192.168.1.7', 'Mozilla/5.0...', INTERVAL '6 days'),
    ('BookNest', 'Book Worm', 'The Great Novel', 'Reader Appreciation Month', '192.168.1.10', 'Mozilla/5.0...', INTERVAL '12 hours'),
    ('BookNest', 'Book Worm', 'The Great Novel', 'Reader Appreciation Month', '192.168.1.11', 'Mozilla/5.0...', INTERVAL '1 day')
) AS v(t_name, a_name, i_name, c_name, ip, ua, offset_interval)
JOIN tenants t ON t.name = v.t_name
JOIN affiliates a ON a.name = v.a_name AND a.tenant_id = t.id
JOIN items i ON i.name = v.i_name AND i.tenant_id = t.id
JOIN campaigns c ON c.name = v.c_name AND c.tenant_id = t.id;

-- 6. Orders
INSERT INTO orders (tenant_id, affiliate_id, item_id, campaign_id, order_value, commission, created_at)
SELECT t.id, a.id, i.id, c.id, v.val, v.comm, NOW() - v.offset_interval
FROM (VALUES
    ('HappyPaws', 'John Barker', 'Premium Dog Food', 'Spring Paws Sale', 45.99, 4.60, INTERVAL '12 hours'),
    ('HappyPaws', 'John Barker', 'Premium Dog Food', 'Spring Paws Sale', 45.99, 4.60, INTERVAL '1 day'),
    ('HappyPaws', 'Sarah Whiskers', 'Cat Grooming Kit', 'Spring Paws Sale', 29.99, 3.00, INTERVAL '2 days'),
    ('TechGear', 'Tech Reviewer Pro', 'Ultra Wireless Mouse', 'Summer Tech Expo', 59.90, 7.49, INTERVAL '5 hours'),
    ('TechGear', 'Gadget Guru', 'Mechanical Keyboard', 'Summer Tech Expo', 120.00, 15.00, INTERVAL '22 hours'),
    ('FitLife', 'Healthy Habit', 'Yoga Membership (Monthly)', 'New Year, New You', 50.00, 4.00, INTERVAL '3 days'),
    ('FitLife', 'Healthy Habit', 'Protein Powder', 'New Year, New You', 35.00, 2.80, INTERVAL '4 days'),
    ('BookNest', 'Book Worm', 'The Great Novel', 'Reader Appreciation Month', 19.99, 3.00, INTERVAL '18 hours')
) AS v(t_name, a_name, i_name, c_name, val, comm, offset_interval)
JOIN tenants t ON t.name = v.t_name
JOIN affiliates a ON a.name = v.a_name AND a.tenant_id = t.id
JOIN items i ON i.name = v.i_name AND i.tenant_id = t.id
JOIN campaigns c ON c.name = v.c_name AND c.tenant_id = t.id;

