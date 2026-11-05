# Lesson 1: Secure Order API

## Goal
Provide an order lookup API that enforces authentication
and customer ownership.

## Implementation
- Spring Security authenticates local demo users with HTTP Basic.
- OrderService checks ownership and permissions.
- OrderResponse exposes only the required order fields.
- Missing and inaccessible orders both return 404.
- Demo users are enabled through the local profile.

## Run
From backend/:

./mvnw spring-boot:run -Dspring-boot.run.profiles=local

## Test
From backend/:

./mvnw test

## Verified scenarios
- Customers can read their own orders.
- Customers cannot read another customer's orders.
- Administrators with orders:read:any can read any order.
- Missing credentials and incorrect passwords return 401.
- Nonexistent orders return 404.
- Request parameters cannot grant permissions.
- Responses omit customerId.

## Agent design principle
The model may propose an order lookup, but the backend
decides whether the authenticated caller can access it.

## Limitations
- Fictional, in-memory orders.
- Public demo credentials for local learning.
- No AI agent connected yet.