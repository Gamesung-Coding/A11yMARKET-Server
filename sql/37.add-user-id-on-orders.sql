-- Migration: Add userId column to ORDERS table
-- This column stores the user's UUID for order ownership verification
-- The existing userEmail column is kept as a snapshot for order records

-- Step 1: Add nullable userId column
ALTER TABLE ORDERS ADD COLUMN user_id UUID;

-- Step 2: Backfill userId from Users table using userEmail
UPDATE ORDERS o
SET user_id = (
    SELECT u.user_id
    FROM USERS u
    WHERE u.user_email = o.user_email
)
WHERE o.user_id IS NULL;

-- Step 3: Make userId NOT NULL after backfill
ALTER TABLE ORDERS ALTER COLUMN user_id SET NOT NULL;

-- Step 4: Create index for userId lookups
CREATE INDEX idx_orders_user_id ON ORDERS (user_id);
