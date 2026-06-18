# Test Plan: Membership Payment Plan

## Story
As a player, I want to choose the payment plan, so I can use one that fits my budget.

## Examples (2 E2E Tests)

### Example 1 — Robert joins Riverside Rovers FC with a single payment of £45.00
### Example 2 — Robert joins Riverside Rovers FC with monthly payments of £6.50

Each test is a full E2E user journey covering all 3 ACs:
- **AC1** — Cost visible when selecting a payment plan (Membership Type page)
- **AC2** — Selected plan and cost visible when entering payment details (Payment page)
- **AC3** — Receipt shows: membership reference, club details, user details, payment details

---

## Application Flow

```
/join  →  /join/details  →  /join/membership  →  /join/payment  →  /join/confirmation
Club       Your Details      Membership Type      Payment           Receipt (Done)
```

---

## UI Locators (data-testid)

| Page            | Key testids                                                                                      |
|-----------------|--------------------------------------------------------------------------------------------------|
| Club            | `club-option-riverside-rovers`, `club-continue`                                                  |
| Details         | `field-first-name`, `field-last-name`, `field-email`, `field-phone`, `field-dob`, `field-address1`, `field-city`, `field-postcode`, `field-country`, `details-continue` |
| Membership Type | `membership-option-one_off`, `price-one_off` → "45.00", `membership-option-monthly`, `price-monthly` → "6.50 / month", `membership-continue` |
| Payment         | `payment-membership`, `payment-amount`, `field-cardholder`, `field-card-number`, `field-expiry`, `field-cvc`, `payment-submit` |
| Confirmation    | `reference`, `summary-club`, `summary-name`, `summary-email`, `summary-phone`, `summary-membership`, `fee-amount`, `summary-card`, `summary-status` |

---

## Test Data

### MemberData record — `domain/MemberData.java` (static — Robert is named in the story)
```
firstName: "Robert", lastName: "Smith"
email: "robert.smith@example.com", phone: "07700900000"
dateOfBirth: "1990-01-15"
addressLine1: "123 Main Street", city: "London", postcode: "SW1A 1AA", country: "United Kingdom"
```

### PaymentCard record — `domain/PaymentCard.java` (static — from guidance)
```
cardholderName: "Robert Smith"
cardNumber: "4242 4242 4242 4242", expiry: "07/29", cvc: "123"
```

---

## Page Objects

| Class                | File                            |
|----------------------|---------------------------------|
| `ClubSelectionPage`  | `pages/ClubSelectionPage.java`  |
| `PlayerDetailsPage`  | `pages/PlayerDetailsPage.java`  |
| `MembershipTypePage` | `pages/MembershipTypePage.java` |
| `PaymentPage`        | `pages/PaymentPage.java`        |
| `ConfirmationPage`   | `pages/ConfirmationPage.java`   |

---

## The 2 Tests

### Test 1 — E2E: Robert joins Riverside Rovers FC with a single payment of £45.00
**Assertions:**
1. (AC1) Membership page: one-off option shows `45.00`
2. (AC2) Payment page: membership type = "One-off", amount = "45.00", submit = "Pay 45.00"
3. (AC3) Receipt: reference starts with "TFP-", club = "Riverside Rovers FC — London, United Kingdom",
   name = "Robert Smith", email, phone, membership = "One-off", fee = "45.00",
   card = "•••• •••• •••• 4242", status = "Succeeded"

### Test 2 — E2E: Robert joins Riverside Rovers FC with monthly payments of £6.50
**Assertions:**
1. (AC1) Membership page: monthly option shows `6.50 / month`
2. (AC2) Payment page: membership type = "Monthly recurring", amount = "6.50 / month", submit = "Pay 6.50"
3. (AC3) Receipt: reference starts with "TFP-", club = "Riverside Rovers FC — London, United Kingdom",
   name = "Robert Smith", membership = "Monthly recurring", fee = "6.50 / month",
   card = "•••• •••• •••• 4242", status = "Succeeded"

> ⚠️ **Known app issue:** The payment page currently shows £45.00 for the monthly plan instead of £6.50.
> Test 2 will assert the correct expected value (£6.50) per the acceptance criteria.
