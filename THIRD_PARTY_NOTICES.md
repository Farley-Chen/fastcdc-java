# Third-party notices and source review

Original fastcdc-java code remains under the root MIT [LICENSE](LICENSE),
Copyright (c) 2023 farley-chen. Upstream portions retain the terms below;
this is attribution and compliance with existing grants, not relicensing.

## Scope and limits

The project author confirms these direct sources: fastcdc-py for the chunker,
jMonkeyEngine RingBuffer for the ring-buffer adaptation, and Apache Commons
Lang for the copied ArrayUtils subset. The author did not consult fastcdc-rs
or ronomon/deduplication when writing the Java implementation.

The versions below were used for this source/license comparison; the exact
versions originally consulted were not recorded.
The review covers embedded implementation portions, not an exhaustive inventory
of build/test dependencies (which are not shaded into this library JAR).

## jMonkeyEngine — BSD-3-Clause

- Local file: `RingByteArray.java`.
- Compared source: [RingBuffer.java at v3.6.1-stable](https://github.com/jMonkeyEngine/jmonkeyengine/blob/4de10c34f6d078e0cd1e32c16bdf5303ceb61e26/jme3-ios/src/main/java/com/jme3/util/RingBuffer.java).
- [Upstream LICENSE.md](https://github.com/jMonkeyEngine/jmonkeyengine/blob/4de10c34f6d078e0cd1e32c16bdf5303ceb61e26/LICENSE.md):
  Copyright (c) 2009-2023 jMonkeyEngine.
- Full copyright, conditions and disclaimer: [licenses/jmonkeyengine-BSD-3-Clause.txt](licenses/jmonkeyengine-BSD-3-Clause.txt).

The four storage/count/index fields, empty/size methods, overflow check,
write/increment/wrap sequence, and field comments correspond to the upstream
queue. This supports treating it as an adaptation, beyond the generic idea of
a circular buffer. Local changes specialize storage to bytes, rename members,
replace pop/iteration with index advancement and range access, and add bulk
operations and validation. The compared upstream source has no file-level
copyright header; the copyright above is taken verbatim from its root license.
BSD conditions require retaining the copyright, conditions and disclaimer in
source and reproducing them in binary distribution materials, plus the
non-endorsement condition. No separate NOTICE requirement appears in that grant.

## Apache Commons Lang — Apache License 2.0

- Local file: `ArrayUtils.java`.
- Compared source: [ArrayUtils.java, rel/commons-lang-3.12.0](https://github.com/apache/commons-lang/blob/rel/commons-lang-3.12.0/src/main/java/org/apache/commons/lang3/ArrayUtils.java).
- [Upstream LICENSE.txt](https://github.com/apache/commons-lang/blob/rel/commons-lang-3.12.0/LICENSE.txt)
  and [NOTICE.txt](https://github.com/apache/commons-lang/blob/rel/commons-lang-3.12.0/NOTICE.txt).
- Full license: [licenses/Apache-2.0.txt](licenses/Apache-2.0.txt).
- Unmodified upstream NOTICE: [licenses/commons-lang-NOTICE.txt](licenses/commons-lang-NOTICE.txt):

> Apache Commons Lang
>
> Copyright 2001-2021 The Apache Software Foundation
>
> This product includes software developed at
> The Apache Software Foundation (https://www.apache.org/).

`EMPTY_BYTE_ARRAY`, byte-array `subarray`, `addAll` and `clone`, including
substantial Javadoc, match this upstream implementation. Local changes extract
a byte-array subset, change package/imports and formatting/documentation. This
is code/documentation reuse, not merely an array-processing idea. The original
ASF license header is retained and a prominent modification notice is added.
Apache-2.0 section 4 requires a license copy, notices of modified files,
retention of applicable source notices, and reproduction of relevant upstream
NOTICE attribution. The complete compared NOTICE is shipped without edits.

## FastCDC implementation lineage — MIT

### iscc/fastcdc-py

- Compared version: v1.5.0, commit `be08441c2a431822f9ce801b8c6093c33e836a68`.
- [fastcdc/fastcdc_py.py](https://github.com/iscc/fastcdc-py/blob/be08441c2a431822f9ce801b8c6093c33e836a68/fastcdc/fastcdc_py.py)
  and [LICENSE](https://github.com/iscc/fastcdc-py/blob/be08441c2a431822f9ce801b8c6093c33e836a68/LICENSE).
- Copyright (c) 2020 Titusz Pan
- Full grant and disclaimer: [licenses/fastcdc-py-MIT.txt](licenses/fastcdc-py-MIT.txt).

The source comparison identifies the following adapted portions:

- `ChunkIterator`: chunk scanning and stream-refill logic corresponding to
  `cdc_offset` and `chunk_generator`.
- `Utils`: the arithmetic helpers `logarithm2`, `ceil_div`, `center_size` and
  `mask`; the Java stream-reading helper is local.
- `Const`: the six size bounds, their comments and the 256-entry Gear table
  (the Python decimal values are expressed as Java hexadecimal literals).

The Java implementation adds its own iterator/API, ring-buffer integration,
hash callbacks, validation and short-input handling. Attribution here applies
to the adapted portions, not to each entire Java file. No distinct copied
implementation was identified in the `FastCDC` factory or `FastCDCOption`;
shared parameter defaults and the FastCDC algorithm itself are not treated as
code reuse by themselves.

### Earlier implementation lineage

The [Python v1.5.0 README](https://github.com/iscc/fastcdc-py/blob/be08441c2a431822f9ce801b8c6093c33e836a68/README.md)
describes its origin as a port of fastcdc-rs and its implementation as a
translation of ronomon/deduplication. The
[Rust 1.0.0 README](https://github.com/nlfiedler/fastcdc-rs/blob/d964c01c6fdc943803492fa39c5def5151f32806/README.md)
also identifies Ronomon as its implementation source.

The earlier MIT notices are retained centrally for implementation portions
inherited through Python. This is supported by the documented translation
lineage and the corresponding center-size calculation and two-stage scan,
not merely by links to earlier projects. It does not claim that the Java
author consulted those repositories or assign the whole Java implementation
to their authors.

- [fastcdc-rs 1.0.0 src/lib.rs](https://github.com/nlfiedler/fastcdc-rs/blob/d964c01c6fdc943803492fa39c5def5151f32806/src/lib.rs),
  [src/table.rs](https://github.com/nlfiedler/fastcdc-rs/blob/d964c01c6fdc943803492fa39c5def5151f32806/src/table.rs)
  and [LICENSE](https://github.com/nlfiedler/fastcdc-rs/blob/d964c01c6fdc943803492fa39c5def5151f32806/LICENSE):
  Copyright (c) 2019 Nathan Fiedler.
  [Full MIT text](licenses/fastcdc-rs-MIT.txt).
- [ronomon/deduplication binding.js](https://github.com/ronomon/deduplication/blob/30b4800d151cb8c9ad99deb58e9aa2eca51baf6e/binding.js)
  and [LICENSE](https://github.com/ronomon/deduplication/blob/30b4800d151cb8c9ad99deb58e9aa2eca51baf6e/LICENSE):
  Copyright (c) 2017 Ronomon.
  [Full MIT text](licenses/ronomon-MIT.txt).
  This is a pinned comparison snapshot, not an identified original import.

All four compared implementations contain the same 256 Gear values.
Ronomon's [table.js](https://github.com/ronomon/deduplication/blob/30b4800d151cb8c9ad99deb58e9aa2eca51baf6e/table.js)
generates these values deterministically using AES-256-CTR with zero key, IV
and input, then reduces the values to 31 bits. Table equality establishes a
technical correspondence; this review does not assume that the generated
numbers alone establish copyright or an attribution obligation.

The retained earlier notices cover the documented implementation lineage;
this review does not determine the exact copyright scope of every inherited
fragment or assert that every part independently requires all three notices.

MIT requires retaining copyright and permission notices in copies or
substantial portions; the complete texts here include the disclaimers. These
MIT grants impose no separate NOTICE-file requirement.

## Distribution

The Maven resource configuration copies the root `LICENSE`, this document and
all files in `licenses/` without filtering into `META-INF/` in the binary JAR.
The existing Maven Source Plugin 3.2.1 includes those project resources in the
source JAR as well. Paths are `META-INF/LICENSE`,
`META-INF/THIRD_PARTY_NOTICES.md` and `META-INF/licenses/*`.
Use `mise run build` to generate both artifacts. Source packaging is bound to
the normal Maven `package` phase; publishing and signing remain in the
`central-publish` profile.
