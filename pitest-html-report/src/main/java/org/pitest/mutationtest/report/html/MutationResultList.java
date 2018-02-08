/*
 * Copyright 2010 Henry Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package org.pitest.mutationtest.report.html;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.pitest.functional.FCollection;
import org.pitest.functional.FunctionalIterable;
import org.pitest.functional.SideEffect1;
import org.pitest.mutationtest.MutationResult;

public class MutationResultList implements FunctionalIterable<MutationResult> {

  private final List<MutationResult> impl = new ArrayList<>();

  public MutationResultList(final Collection<MutationResult> results) {
    this.impl.addAll(results);
  }

  public List<MutationGrouping> groupMutationsByLine() {
    sortMutationsIntoLineOrder();
    final List<MutationGrouping> groups = new ArrayList<>();
    List<MutationResult> sublist = new ArrayList<>();
    int lastLineNumber = -1;
    for (final MutationResult each : this.impl) {
      if ((lastLineNumber != each.getDetails().getLineNumber())
          && !sublist.isEmpty()) {
        groups.add(new MutationGrouping(lastLineNumber, "Line "
            + lastLineNumber, sublist));
        sublist = new ArrayList<>();
      }
      sublist.add(each);
      lastLineNumber = each.getDetails().getLineNumber();
    }
    if (!sublist.isEmpty()) {
      groups.add(new MutationGrouping(lastLineNumber, "Line " + lastLineNumber,
          sublist));
    }
    return groups;
  }

  private void sortMutationsIntoLineOrder() {
    final Comparator<MutationResult> c = (o1, o2) -> o1.getDetails().getLineNumber()
        - o2.getDetails().getLineNumber();
    Collections.sort(this.impl, c);
  }

  @Override
  public boolean contains(final Predicate<MutationResult> predicate) {
    return FCollection.contains(this.impl, predicate);
  }

  @Override
  public List<MutationResult> filter(
      final Predicate<MutationResult> predicate) {
    return FCollection.filter(this, predicate);
  }

  @Override
  public <B> List<B> flatMap(
      final Function<MutationResult, ? extends Iterable<B>> f) {
    return FCollection.flatMap(this, f);
  }

  @Override
  public void forEach(final SideEffect1<MutationResult> e) {
    FCollection.forEach(this, e);
  }

  @Override
  public Iterator<MutationResult> iterator() {
    return this.impl.iterator();
  }

  @Override
  public <B> List<B> map(final Function<MutationResult, B> f) {
    return FCollection.map(this, f);
  }

  @Override
  public <B> void mapTo(final Function<MutationResult, B> f,
      final Collection<? super B> bs) {
    FCollection.mapTo(this, f, bs);
  }

}
