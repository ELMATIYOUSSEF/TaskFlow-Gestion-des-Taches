-- Event to check and update rmpToken every hour
DELIMITER //
CREATE EVENT UpdateRmpTokenEvent
ON SCHEDULE EVERY 1 HOUR
DO
BEGIN
UPDATE user u
SET u.dateForDouble = CURRENT_DATE + INTERVAL 2 DAY
WHERE EXISTS (
    SELECT 1
    FROM task_change_request tcr
             JOIN task t ON tcr.task_id = t.id
    WHERE tcr.dateRequest + INTERVAL 12 HOUR < NOW()
        AND tcr.status = 'pending'
        AND t.user_id = u.id
);
END //
DELIMITER ;

-- Event to double rmpToken every day
DELIMITER //
CREATE EVENT DoubleRmpTokenEvent
ON SCHEDULE EVERY 1 DAY
DO
BEGIN
UPDATE user
SET rmpToken = rmpToken * 2
WHERE dateForDouble = CURDATE() + INTERVAL 2 DAY;
END //
DELIMITER ;

-- Even to MArk Tasks as NOt Completed
DELIMITER //
CREATE EVENT MarkTasksAsNotCompleted
ON SCHEDULE EVERY 1 DAY
DO
BEGIN
UPDATE task t
SET t.completed = "NO_COMPLETED"
WHERE t.exp_date < CURRENT_DATE ;
END;
DELIMITER ;
