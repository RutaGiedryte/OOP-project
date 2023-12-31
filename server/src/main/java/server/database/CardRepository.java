
/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package server.database;

import commons.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CardRepository extends JpaRepository<Card, Integer> {
    @Modifying
    @Query("UPDATE Card c SET c.index = c.index + 1 WHERE c.index >= :index AND c.listId = :listId")
    void shiftCardsRight(int index, int listId);

    @Modifying
    @Query("UPDATE Card c SET c.index = c.index - 1 WHERE c.index > :index AND c.listId = :listId")
    void shiftCardsLeft(int index, int listId);

    @Modifying
    @Query("UPDATE Card c SET c.index = c.index + 1 WHERE c.index < :index2 " +
            "AND c.index>= :index1 AND c.listId = :listId")
    void shiftCardsBetweenDown(int index1,int index2,  int listId);

    @Modifying
    @Query("UPDATE Card c SET c.index = c.index - 1 WHERE c.index > :index1 " +
            "AND c.index <= :index2 AND c.listId = :listId")
    void shiftCardsBetweenUp(int index1,int index2,  int listId);





}

