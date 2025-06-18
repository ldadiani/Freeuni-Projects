#include <stdio.h>
#include <unistd.h>
#include <stdlib.h>
#include <assert.h>
#include <inttypes.h>

#include "teller.h"
#include "account.h"
#include "error.h"
#include "debug.h"
#include "branch.h"

/*
 * deposit money into an account
 */
int
Teller_DoDeposit(Bank *bank, AccountNumber accountNum, AccountAmount amount)
{
  assert(amount >= 0);
  DPRINTF('t', ("Teller_DoDeposit(account 0x%"PRIx64" amount %"PRId64")\n",
                accountNum, amount));

  Account *account = Account_LookupByNumber(bank, accountNum);

  if (account == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }

  BranchID id = (BranchID) (accountNum >> 32);
  sem_wait(&(account->lock));
  sem_wait(&(bank->branches[id].lockBranch));
  Account_Adjust(bank,account, amount, 1);
  sem_post(&(account->lock));
  sem_post(&(bank->branches[id].lockBranch));
  //printf("5\n");
  return ERROR_SUCCESS;
}

/*
 * withdraw money from an account
 */
int
Teller_DoWithdraw(Bank *bank, AccountNumber accountNum, AccountAmount amount)
{
  assert(amount >= 0);

  DPRINTF('t', ("Teller_DoWithdraw(account 0x%"PRIx64" amount %"PRId64")\n",
                accountNum, amount));

  Account *account = Account_LookupByNumber(bank, accountNum);

  if (account == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }

  BranchID id = (BranchID) (accountNum >> 32);

  sem_wait(&(account->lock));
  sem_wait(&(bank->branches[id].lockBranch));

  if (amount > Account_Balance(account)) {
    sem_post(&(account->lock));
    sem_post(&(bank->branches[id].lockBranch));
    //printf("6\n");
    return ERROR_INSUFFICIENT_FUNDS;
  }

  Account_Adjust(bank,account, -amount, 1);

  sem_post(&(account->lock));
  sem_post(&(bank->branches[id].lockBranch));
  //printf("7\n");
  return ERROR_SUCCESS;
}

/*
 * do a tranfer from one account to another account
 */
int
Teller_DoTransfer(Bank *bank, AccountNumber srcAccountNum,
                  AccountNumber dstAccountNum,
                  AccountAmount amount)
{
  
  assert(amount >= 0);

  DPRINTF('t', ("Teller_DoTransfer(src 0x%"PRIx64", dst 0x%"PRIx64
                ", amount %"PRId64")\n",
                srcAccountNum, dstAccountNum, amount));

  Account *srcAccount = Account_LookupByNumber(bank, srcAccountNum);
  if (srcAccount == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }

  Account *dstAccount = Account_LookupByNumber(bank, dstAccountNum);
  if (dstAccount == NULL) {
    return ERROR_ACCOUNT_NOT_FOUND;
  }

  if(srcAccount == dstAccount)  return ERROR_SUCCESS;

  BranchID sourceID = (BranchID) (srcAccountNum >> 32);;
  BranchID destID = (BranchID) (dstAccountNum >> 32);;

  if (sourceID == destID) {
    if(srcAccount->accountNumber < dstAccount->accountNumber) {
      sem_wait(&(srcAccount->lock));
      sem_wait(&(dstAccount->lock));
    } else {
      sem_wait(&(dstAccount->lock));
      sem_wait(&(srcAccount->lock));
    }
  } else {
    if (sourceID < destID) { 
      sem_wait(&(srcAccount->lock));
      sem_wait(&(dstAccount->lock));
      sem_wait(&(bank->branches[sourceID].lockBranch));
      sem_wait(&(bank->branches[destID].lockBranch));
    } else {
      sem_wait(&(dstAccount->lock));
      sem_wait(&(srcAccount->lock));
      sem_wait(&(bank->branches[destID].lockBranch));
      sem_wait(&(bank->branches[sourceID].lockBranch));
    }
  }

  if (amount > Account_Balance(srcAccount)) {
    sem_post(&(srcAccount->lock));
    sem_post(&(dstAccount->lock));
    if (sourceID != destID) {
      sem_post(&(bank->branches[sourceID].lockBranch));
      sem_post(&(bank->branches[destID].lockBranch));
    } 
    //printf("8\n");
    return ERROR_INSUFFICIENT_FUNDS;
  }


int updateBranch = !Account_IsSameBranch(srcAccountNum, dstAccountNum);

  Account_Adjust(bank, srcAccount, -amount, updateBranch);
  Account_Adjust(bank, dstAccount, amount, updateBranch);

  sem_post(&(srcAccount->lock));
  sem_post(&(dstAccount->lock));
  //printf("9\n");
  if (sourceID != destID) {
    sem_post(&(bank->branches[sourceID].lockBranch));
    sem_post(&(bank->branches[destID].lockBranch));
  } 
  //printf("10\n");
  return ERROR_SUCCESS;
}