

import java.util.Arrays;

/**
 * Implements a bit array container.
 * @author gianluca
 */
public class BitArray {
  /**
   * Size of the storage type used to implement the container.
   * Current implementation uses ints implying a 32 bit storage size.
   */
  static final int STORAGE_SIZE = 32;
  
  /**
   * The container holding the data
   */
  int[] data;
  
  /**
   * number of bits currently held by the container.
   */
  int count;
  
  /**
   * Capacity of this container expressed in terms of number
   * of bits that can be stored before augmenting the container size.
   */
  int capacity;
  
  /**
   * Creates a new container with given capacity. The container will 
   *    alloc enough memory to store the given number of bits. It will
   *    automatically grow to store more if needed.
   * @param capacity of the container expressed in terms of number of bits that it will hold.
   */
  public BitArray(int capacity) {
    count = 0;
    int nInts = capacity / STORAGE_SIZE + 1;
    data = new int[nInts];
    capacity = data.length * STORAGE_SIZE;
  }
 
  private void updateCapacity() {
    if(count >= capacity) {
      int nInts = data.length;
      nInts *= 2;
      data = Arrays.copyOf(data, nInts);
      capacity = data.length * STORAGE_SIZE;
    }
  }
  
  /**
   * Sets the element at index pos to equal the given bit. The given
   * position needs to be larger than 0 and less than size().
   * @param bit the value of the bit to be set
   * @param pos the position of the bit to be set
   */
  public void set(int pos, int bit) {
    if(pos >= count) {
      throw new ArrayIndexOutOfBoundsException();
    }
    int dataIndex = pos / STORAGE_SIZE;
    int bitIndex = pos % STORAGE_SIZE;

    if(bit == 0) {
      data[dataIndex] &= ~(1 << bitIndex);
    } else if(bit == 1) {
      data[dataIndex] |= 1 << bitIndex;
    } else {
      throw new IllegalArgumentException("bit should be 1 or 0");
    }
  }
  
  /**
   * Append the given bit to the current container.
   * @param bit
   */
  public void add(int bit) {
    updateCapacity();
 
    set(count++, bit);
   }
  
  /**
   * @param index the index to be read.
   * @return the value of the bit at the given index.
   */
  public int get(int index) {
    if(index >= count) {
      throw new IllegalArgumentException();
    }
    
    int dataIndex = index / STORAGE_SIZE;
    int bitIndex = index % STORAGE_SIZE;
    return ((data[dataIndex] & (1 << bitIndex)) == 0) ? 0 : 1;
  }
  
  /**
   * @return the current size of this bit array.
   */
  public int size() {
    return count;
  }
  
  @Override
  public int hashCode() {
    // cheap alternative: return toString().hashCode();
    int dataIndex = count / STORAGE_SIZE;
    int result = 31 * 0xF0F0F0F0;
    
    result ^= 31 * count;
    for(int i=0; i<=dataIndex; ++i) {
      result ^= data[i];
    }
    
    return result;
  }
  
  @Override
  public boolean equals(Object other) {
    if(other == null || count != ((BitArray)other).count) {
      return false;
    }
    
    int dataIndex = count / STORAGE_SIZE;
    
    for(int i=0; i<=dataIndex; ++i) {
      if(data[i] != ((BitArray)other).data[i]) {
        return false;
      }
    } 
    
    return true;
  }
  
  @Override
  public String toString() {
    String result = "";
    for(int i=0; i<count; ++i) {
      result += get(i);
    }
    
    return result;
  }
}
