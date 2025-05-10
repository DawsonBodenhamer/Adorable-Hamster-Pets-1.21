# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

---

## [1.0.1] - 2025-05-10

### Fixed
- Resolved issue where sitting hamsters could still be tempted by items, causing them to move (`HamsterTemptGoal`).
- Corrected wild hamster sleeping behavior: they now consistently wake up when a player approaches, regardless of the player's sneaking status or held item (`HamsterSleepGoal`).
- Adjusted baby hamster rendering:
    - Corrected model scaling logic to properly apply and reset transformations for baby and adult states (`HamsterModel`).
    - Implemented differential scaling for baby hamsters, resulting in a relatively larger head compared to their body (`HamsterModel`).
    - Adjusted baby hamster model's vertical position to prevent slight floating (`HamsterModel`).
    - Reduced shadow size for baby hamsters to better match their smaller model (`HamsterRenderer`).

### Changed
- (Internal) Reorganized code structure and added/updated comments for `HamsterFleeGoal.java`, `HamsterSleepGoal.java`, and `HamsterTemptGoal.java` for improved readability.
- (Internal) Tweaked the "begging" animation to slightly shift the hamster model forward for better visual positioning (`anim_hamster.json`).

### Documentation
- Updated `README.md` and item tooltip for the guide book to remove references to an in-book command for re-obtaining it. Now it's truly a one-time item (unless you have access to creative mode.)

---

## [1.0.0] - 2025-4-10

### First public version of Adorable Hamster Pets. Hello world!