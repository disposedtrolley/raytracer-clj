# raytracer-clj

A raytracer implemented in Clojure based on [The Ray Tracer Challenge](http://www.raytracerchallenge.com). Companion of [disposedtrolley/raytracer](https://github.com/disposedtrolley/raytracer).

## Development

### Prerequisites

- JDK 11, i.e. `brew install openjdk@11`
- [Clojure CLI tools](https://clojure.org/guides/getting_started#_clojure_installer_and_cli_tools)

### Examples

The `tick` namespace provides two `run` functions to showcase the current state of the raytracer.

- `run` simulates a projectile moving through an environment after it's fired. The various positions of the projectile result in an arc.
  - `clj -X raytracer.examples.tick/run`
  - `clj -X raytracer.examples.tick/run :velocity-multiplier 4`
- `run-with-canvas` performs the same simulation as `run`, but will plot the positions of the projectile on a canvas which is then saved as a PPM file. The canvas is fixed-size so the velocity cannot be adjusted via a multiplier.
  - `clj -X raytracer.examples.tick/run-with-canvas :outfile '"/full/path/to/outfile"'`

### Tests

- `bin/kaocha`