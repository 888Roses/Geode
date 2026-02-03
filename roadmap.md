# 🛣️ Version 2.0 Roadmap

## 🛠️ Registration

▢ Registration of `StatusEffect`.

▢ Registration of `LootTable`.

▢ Custom container component modifiers.

▢ Registering `BlockItem` in an easier, more straight-forward way.

▢ Rename `Geode#register` to `Geode#finalize`.

## 📖 Data Generation

▢ Data generation for `StatusEffect`.

▢ Data generation for `LootTable`.

▢ Data generation for complex block states.

## 📐 Math

▢ Evaluating a mathematical operation contained in a `String`.

▢ Adding a `Vec3i` to a `double3` and every other operation.

▢ Adding a `Direction` to a `double3`.

▢ Creating a `double3` at the center of a given `BlockPos` by using `double3#centerOf(BlockPos)`.

## 💡 Events

▢ `AllowEatingCallback` called when an entity tries to eat. Returns whether the entity is allowed to eat or not.

▢ `AllowFallDamageCallback` called when an entity takes fall damage. Returns whether the entity can take fall damage or not.

▢ `AllowRidingCallback` called when an entity tries to ride another entity. Returns whether the entity can ride the other entity or not.

## 🏷️ Debug

▢ Drawing a dotted gizmo line using `Draw#dottedLine(double3, double3, int, float)` and every other common line drawing variants.
