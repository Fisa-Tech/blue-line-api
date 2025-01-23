-- liquibase formatted sql

-- changeset aurel:1737648966788-1
ALTER TABLE challenge_completion ADD completion_date TIMESTAMP(6) WITHOUT TIME ZONE;

-- changeset aurel:1737648966788-2
ALTER TABLE challenge_completion ADD distance_achieved FLOAT(53);

-- changeset aurel:1737648966788-3
ALTER TABLE challenge ADD distance_goal FLOAT(53);

-- changeset aurel:1737648966788-4
ALTER TABLE challenge ADD end_date TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL;

-- changeset aurel:1737648966788-5
ALTER TABLE challenge ADD start_date TIMESTAMP(6) WITHOUT TIME ZONE NOT NULL;

-- changeset aurel:1737648966788-6
ALTER TABLE challenge ADD streak_nb_of_participations INTEGER;

-- changeset aurel:1737648966788-7
ALTER TABLE challenge ADD streak_period VARCHAR(255);

-- changeset aurel:1737648966788-8
ALTER TABLE challenge_completion ADD time_achieved BIGINT;

-- changeset aurel:1737648966788-9
ALTER TABLE challenge ADD time_goal BIGINT;

-- changeset aurel:1737648966788-10
ALTER TABLE challenge_completion DROP COLUMN "completionDate";

-- changeset aurel:1737648966788-11
ALTER TABLE challenge_completion DROP COLUMN "distanceAchieved";

-- changeset aurel:1737648966788-12
ALTER TABLE challenge DROP COLUMN "distanceGoal";

-- changeset aurel:1737648966788-13
ALTER TABLE challenge DROP COLUMN "endDate";

-- changeset aurel:1737648966788-14
ALTER TABLE challenge DROP COLUMN "startDate";

-- changeset aurel:1737648966788-15
ALTER TABLE challenge DROP COLUMN "streakNbOfParticipations";

-- changeset aurel:1737648966788-16
ALTER TABLE challenge DROP COLUMN "streakPeriod";

-- changeset aurel:1737648966788-17
ALTER TABLE challenge_completion DROP COLUMN "timeAchieved";

-- changeset aurel:1737648966788-18
ALTER TABLE challenge DROP COLUMN "timeGoal";

