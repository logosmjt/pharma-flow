CREATE TABLE "drug" (
  "id" uuid PRIMARY KEY NOT NULL,
  "name" varchar NOT NULL,
  "manufacturer" varchar NOT NULL,
  "batch_number" varchar NOT NULL,
  "expiry_date" date NOT NULL,
  "stock" int NOT NULL,
  "created_at" timestamp NOT NULL,
  "updated_at" timestamp NOT NULL
);

CREATE TABLE "pharmacy" (
  "id" uuid PRIMARY KEY NOT NULL,
  "name" varchar NOT NULL,
  "location" varchar NOT NULL,
  "created_at" timestamp NOT NULL,
  "updated_at" timestamp NOT NULL
);

CREATE TABLE "pharmacy_drug_allocation" (
  "id" uuid PRIMARY KEY NOT NULL,
  "pharmacy_id" uuid NOT NULL,
  "drug_id" uuid NOT NULL,
  "allocation_limit" int NOT NULL,
  "created_at" timestamp NOT NULL,
  "updated_at" timestamp NOT NULL
);

CREATE TABLE "prescription" (
  "id" uuid PRIMARY KEY NOT NULL,
  "patient_id" uuid NOT NULL,
  "pharmacy_id" uuid NOT NULL,
  "status" varchar NOT NULL,
  "fulfilled_at" timestamp,
  "created_at" timestamp NOT NULL,
  "updated_at" timestamp NOT NULL
);

CREATE TABLE "prescription_drug" (
  "id" uuid PRIMARY KEY NOT NULL,
  "prescription_id" uuid NOT NULL,
  "drug_id" uuid NOT NULL,
  "quantity" int NOT NULL,
  "created_at" timestamp NOT NULL,
  "updated_at" timestamp NOT NULL
);

CREATE TABLE "audit_log" (
  "id" uuid PRIMARY KEY NOT NULL,
  "prescription_id" uuid NOT NULL,
  "patient_id" uuid NOT NULL,
  "pharmacy_id" uuid NOT NULL,
  "requested_drugs" varchar NOT NULL,
  "dispensed_drugs" varchar,
  "success" boolean NOT NULL,
  "failure_reason" varchar,
  "created_at" timestamp NOT NULL,
  "updated_at" timestamp NOT NULL
);

CREATE UNIQUE INDEX ON "drug" ("manufacturer", "batch_number");

CREATE UNIQUE INDEX ON "pharmacy_drug_allocation" ("pharmacy_id", "drug_id");

ALTER TABLE "pharmacy_drug_allocation" ADD FOREIGN KEY ("pharmacy_id") REFERENCES "pharmacy" ("id");

ALTER TABLE "pharmacy_drug_allocation" ADD FOREIGN KEY ("drug_id") REFERENCES "drug" ("id");

ALTER TABLE "prescription" ADD FOREIGN KEY ("pharmacy_id") REFERENCES "pharmacy" ("id");

ALTER TABLE "prescription_drug" ADD FOREIGN KEY ("prescription_id") REFERENCES "prescription" ("id");

ALTER TABLE "prescription_drug" ADD FOREIGN KEY ("drug_id") REFERENCES "drug" ("id");

ALTER TABLE "audit_log" ADD FOREIGN KEY ("prescription_id") REFERENCES "prescription" ("id");

ALTER TABLE "audit_log" ADD FOREIGN KEY ("pharmacy_id") REFERENCES "pharmacy" ("id");
