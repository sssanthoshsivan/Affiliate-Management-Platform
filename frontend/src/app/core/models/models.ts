export interface Tenant {
  id: number;
  name: string;
  domain: string;
  status: string;
  commissionRate: number;
  createdAt: string;
}

export interface Affiliate {
  id: number;
  tenantId: number;
  name: string;
  email: string;
  status: string;
  createdAt: string;
}

export interface Item {
  id: number;
  tenantId: number;
  name: string;
  type: 'PRODUCT' | 'SERVICE';
  category: string;
  price: number;
  createdAt: string;
}

export interface Campaign {
  id: number;
  tenantId: number;
  name: string;
  description: string;
  createdAt: string;
}

export interface AffiliateLink {
  id: number;
  tenantId: number;
  affiliateId: number;
  itemId: number;
  campaignId: number;
  shortCode: string;
  trackingCode: string;
  createdAt: string;
}

export interface Analytics {
  totalClicks: number;
  totalOrders: number;
  totalRevenue: number;
  totalCommission: number;
  topAffiliates: AffiliatePerformance[];
}

export interface AffiliatePerformance {
  affiliateId: number;
  affiliateName: string;
  orders: number;
  revenue: number;
  commission: number;
}
